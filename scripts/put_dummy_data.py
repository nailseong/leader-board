import requests
import random
import concurrent.futures
import time
from typing import List

# 1. 테스트 파라미터 설정
NUM_PLAYERS = 500  # 플레이어 수
NUM_GAMES = NUM_PLAYERS * 10 * 30  # 게임 진행 횟수
NUM_WORKERS = 10  # 동시 요청을 위한 워커 수

# API 엔드포인트
GAME_API_URL =  "http://localhost:8082/games"
RANKING_API_URL = "http://localhost:8080/rankings"

def play_game(player1: str, player2: str) -> None:
    """게임을 진행합니다."""
    try:
        # 게임 진행
        game_response = requests.post(
            GAME_API_URL,
            json={"leftPlayer": player1, "rightPlayer": player2}
        )
        game_response.raise_for_status()
    except Exception as e:
        print(f"게임 진행 중 오류 발생: {e}")

def get_rankings() -> dict:
    """상위 10명의 랭킹을 조회합니다."""
    try:
        response = requests.get(f"{RANKING_API_URL}?size=10")
        response.raise_for_status()
        return response.json()
    except Exception as e:
        print(f"랭킹 조회 중 오류 발생: {e}")
        return {"count": 0, "rankingInfos": []}

def main():
    # 플레이어 목록 생성
    players = [f"player_{i}" for i in range(1, NUM_PLAYERS + 1)]
    
    print(f"테스트 시작: {NUM_PLAYERS}명의 플레이어, {NUM_GAMES}게임")
    start_time = time.time()
    
    # 게임 진행
    with concurrent.futures.ThreadPoolExecutor(max_workers=NUM_WORKERS) as executor:
        futures = []
        for _ in range(NUM_GAMES):
            player1, player2 = random.sample(players, 2)
            futures.append(executor.submit(play_game, player1, player2))
        
        # 모든 게임이 완료될 때까지 대기
        concurrent.futures.wait(futures)
    
    # 랭킹 조회 및 출력
    rankings = get_rankings()
    
    print("\n=== 테스트 리포트 ===")
    print(f"총 게임 수: {NUM_GAMES}")
    print(f"실행 시간: {time.time() - start_time:.2f}초")
    print(f"\n상위 {rankings['count']}명 랭킹:")
    for rank_info in rankings['rankingInfos']:
        print(f"{rank_info['rank']}. {rank_info['player']}: {rank_info['score']}점")

if __name__ == "__main__":
    main() 