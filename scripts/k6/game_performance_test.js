import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

// 테스트 파라미터 설정
const NUM_PLAYERS = 10;
const NUM_WORKERS = 10;
const GAMES_PER_SECOND = 50;

// API 엔드포인트
const GAME_API_URL = "http://localhost:8082/games";
const RANKING_API_URL = "http://localhost:8080/rankings";
const SCORE_API_URL = "http://localhost:8080/scores";

// 플레이어 목록 생성
const players = new SharedArray('players', function() {
    return Array.from({ length: NUM_PLAYERS }, (_, i) => `player_${i + 1}`);
});

// K6 옵션 설정
export const options = {
    scenarios: {
        // 초기화 시나리오
        initialize: {
            executor: 'per-vu-iterations',
            vus: 1,
            iterations: 1,
            maxDuration: '30s',
            exec: 'initializeAllPlayerScores',
        },
        // 게임 진행 시나리오
        gameplay: {
            executor: 'constant-arrival-rate',
            rate: GAMES_PER_SECOND,
            timeUnit: '1s',
            duration: '30s',
            preAllocatedVUs: 50,
            maxVUs: 100,
            exec: 'playGame',
            startTime: '5s',
        },
        // 랭킹 조회 시나리오
        // ranking: {
        //     executor: 'ramping-arrival-rate',
        //     startRate: 1,
        //     timeUnit: '1s',
        //     preAllocatedVUs: 5,
        //     maxVUs: 20,
        //     stages: [
        //         { duration: '30s', target: 5 },
        //         { duration: '1m', target: 5 },
        //         { duration: '30s', target: 10 },
        //         { duration: '1m', target: 10 },
        //     ],
        //     exec: 'getRankings',
        //     startTime: '5s',
        // }
    },
    thresholds: {
        http_req_duration: ['p(95)<500'],
        'http_req_duration{type:game}': ['p(95)<300'],
        'http_req_duration{type:ranking}': ['p(95)<200'],
        'http_req_duration{type:score}': ['p(95)<150'],
    },
};

// 모든 플레이어 점수 초기화 함수
export function initializeAllPlayerScores() {
    console.log(`모든 플레이어(${players.length}명)의 점수 초기화 중...`);

    // 각 플레이어마다 순차적으로 점수 초기화
    for (let i = 0; i < players.length; i++) {
        const player = players[i];

        // Content-Type을 명시적으로 application/json으로 설정
        const params = {
            headers: {
                'Content-Type': 'application/json'
            },
            tags: { type: 'score' }
        };

        const payload = JSON.stringify({ player: player });

        const response = http.put(SCORE_API_URL, payload, params);

        const success = response.status === 200;

        if (success) {
            console.log(`플레이어 ${player} 점수 초기화 성공`);
        } else {
            console.warn(`플레이어 ${player} 점수 초기화 실패: ${response.status}, 응답: ${response.body}`);
        }

        // API 서버 과부하 방지를 위한 짧은 휴식
        sleep(0.2);
    }

    console.log(`모든 플레이어 점수 초기화 완료`);

    // 초기화 후 현재 랭킹 출력
    const rankingResponse = http.get(`${RANKING_API_URL}?size=10`, {
        tags: { type: 'initial_ranking' }
    });

    if (rankingResponse.status === 200) {
        const rankings = JSON.parse(rankingResponse.body);
        console.log(`\n초기화 후 랭킹 확인:`);

        const rankingInfos = rankings.rankingInfos || [];
        if (rankingInfos.length > 0) {
            rankingInfos.forEach(info => {
                console.log(`${info.rank}. ${info.player}: ${info.score}점`);
            });
        } else {
            console.log(`랭킹 정보가 없습니다.`);
        }
    } else {
        console.warn(`랭킹 조회 실패: ${rankingResponse.status}`);
    }

    return true;
}

// 게임 진행 함수
export function playGame() {
    // 무작위로 두 플레이어 선택
    const playerIndexes = Array.from(
        { length: 2 },
        () => randomIntBetween(0, players.length - 1)
    );

    // 동일 플레이어가 선택되지 않도록 보장
    if (playerIndexes[0] === playerIndexes[1]) {
        playerIndexes[1] = (playerIndexes[1] + 1) % players.length;
    }

    const player1 = players[playerIndexes[0]];
    const player2 = players[playerIndexes[1]];

    const response = http.post(GAME_API_URL, JSON.stringify({
        leftPlayer: player1,
        rightPlayer: player2
    }), {
        headers: { 'Content-Type': 'application/json' },
        tags: { type: 'game' }
    });

    check(response, {
        'game played': (r) => r.status === 200,
    });

    // API 과부하 방지를 위한 짧은 휴식
    sleep(0.1);
}

// 랭킹 조회 함수
export function getRankings() {
    const response = http.get(`${RANKING_API_URL}?size=10`, {
        tags: { type: 'ranking' }
    });

    check(response, {
        'rankings retrieved': (r) => r.status === 200,
        'rankings contains data': (r) => {
            const body = JSON.parse(r.body);
            return body.rankingInfos && body.rankingInfos.length > 0;
        }
    });

    // API 과부하 방지를 위한 짧은 휴식
    sleep(0.5);
}

// 테스트 시작 전 출력
export function setup() {
    console.log(`테스트 시작: ${NUM_PLAYERS}명의 플레이어, ${GAMES_PER_SECOND}req/s 속도로 게임 진행`);
    return { startTime: new Date() };
}

// 테스트 종료 후 출력
export function teardown(data) {
    const testDuration = (new Date() - data.startTime) / 1000;
    console.log(`\n=== 테스트 리포트 ===`);
    console.log(`실행 시간: ${testDuration.toFixed(2)}초`);

    // 마지막 랭킹 조회
    const response = http.get(`${RANKING_API_URL}?size=10`, {
        tags: { type: 'final_ranking' }
    });

    if (response.status === 200) {
        const rankings = JSON.parse(response.body);
        console.log(`\n상위 ${rankings.count || rankings.rankingInfos.length}명 랭킹:`);
        rankings.rankingInfos.forEach(info => {
            console.log(`${info.rank}. ${info.player}: ${info.score}점`);
        });
    }
}