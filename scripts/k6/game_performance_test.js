import http from 'k6/http';
import {check, sleep} from 'k6';
import {SharedArray} from 'k6/data';
import {randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

// 테스트 파라미터 설정
const NUM_PLAYERS = 50;
const TEST_DURATION = '180s';
const GAMES_PER_SECOND = 5000;

// API 엔드포인트
const GAME_API_URL = "http://localhost:8082/games";
const RANKING_API_URL = "http://localhost:8080/rankings";
const SCORE_API_URL = "http://localhost:8080/scores";

// 플레이어 목록 생성
const players = new SharedArray('players', function () {
    return Array.from({length: NUM_PLAYERS}, (_, i) => `player_${i + 1}`);
});

// K6 옵션 설정
export const options = {
    scenarios: {
        // 게임 진행 시나리오
        gameplay: {
            executor: 'constant-arrival-rate',
            rate: GAMES_PER_SECOND,
            timeUnit: '1s',
            duration: TEST_DURATION,
            preAllocatedVUs: 1000,
            maxVUs: 2000,
            exec: 'playGame',
            startTime: '5s',
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<500'],
        'http_req_duration{type:game}': ['p(95)<300'],
        'http_req_duration{type:ranking}': ['p(95)<200'],
        'http_req_duration{type:score}': ['p(95)<150'],
    },
};

// 게임 진행 함수
export function playGame() {
    // 무작위로 두 플레이어 선택
    const playerIndexes = Array.from(
        {length: 2},
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
        headers: {'Content-Type': 'application/json'},
        tags: {type: 'game'}
    });

    check(response, {
        'game played': (r) => r.status === 200,
    });

    // API 과부하 방지를 위한 짧은 휴식
    sleep(0.1);
}

// 테스트 시작 전 출력
export function setup() {
    console.log(`테스트 시작: ${NUM_PLAYERS}명의 플레이어, ${GAMES_PER_SECOND}req/s 속도로 게임 진행`);
    console.log(`모든 플레이어(${players.length}명)의 점수 초기화 중...`);

    // 각 플레이어마다 순차적으로 점수 초기화
    for (let i = 0; i < players.length; i++) {
        const player = players[i];

        const params = {
            headers: {
                'Content-Type': 'application/json'
            },
            tags: {type: 'init_score'}
        };

        const payload = JSON.stringify({player: player});

        const response = http.put(SCORE_API_URL, payload, params);

        const success = response.status === 200;

        if (success) {
            console.log(`플레이어 ${player} 점수 초기화 성공`);
        } else {
            console.warn(`플레이어 ${player} 점수 초기화 실패: ${response.status}, 응답: ${response.body}`);
        }
    }

    console.log(`모든 플레이어 점수 초기화 완료`);

    return {startTime: new Date()};
}

// 테스트 종료 후 출력
export function teardown(data) {
    const testDuration = (new Date() - data.startTime) / 1000;
    console.log(`\n=== 테스트 리포트 ===`);
    console.log(`실행 시간: ${testDuration.toFixed(2)}초`);

    // 마지막 랭킹 조회
    const response = http.get(`${RANKING_API_URL}?size=10`, {tags: {type: 'get_score'}});

    if (response.status === 200) {
        const rankings = JSON.parse(response.body);
        console.log(`\n상위 ${rankings.count || rankings.rankingInfos.length}명 랭킹:`);
        rankings.rankingInfos.forEach(info => {
            console.log(`${info.rank}. ${info.player}: ${info.score}점`);
        });
    }
}