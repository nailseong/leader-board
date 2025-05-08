# leader board

![v1.png](img/design/v1.png)

## k factor

![rating_by_K_factor](https://github-production-user-asset-6210df.s3.amazonaws.com/68512686/437699969-631f032e-55fd-41e6-8c38-906374b1d524.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20250426%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250426T101924Z&X-Amz-Expires=300&X-Amz-Signature=c2dd6be30389bfaf324cd4c7c5d7d8d2b81fc68e03a4fbb1abb0d4a6be14c850&X-Amz-SignedHeaders=host)

| 플레이어      | STATIC_K_FACTOR_32 | DYNAMIC_K_FACTOR | SIGMOID_K_FACTOR_SLOPE_5 | SIGMOID_K_FACTOR_SLOPE_10 | SIGMOID_K_FACTOR_SLOPE_15 |
|-----------|--------------------|------------------|--------------------------|---------------------------|---------------------------|
| player_2  | 1259               | 1279             | 1355                     | 1472                      | 1552                      |
| player_8  | 1235               | 1257             | 1341                     | 1457                      | 1538                      |
| player_7  | 1225               | 1248             | 1330                     | 1446                      | 1532                      |
| player_1  | 1221               | 1243             | 1328                     | 1444                      | 1527                      |
| player_5  | 1209               | 1228             | 1313                     | 1431                      | 1515                      |
| player_10 | 1193               | 1216             | 1301                     | 1412                      | 1492                      |
| player_9  | 1189               | 1211             | 1298                     | 1404                      | 1487                      |
| player_6  | 1176               | 1200             | 1290                     | 1401                      | 1484                      |
| player_4  | 1149               | 1174             | 1271                     | 1385                      | 1467                      |
| player_3  | 1144               | 1168             | 1258                     | 1370                      | 1456                      |

## todo

- [ ] game event listener 분리
    - [ ] db 부하가 심하지 않도록 배압조절
    - [ ] 성능테스트 다시 해보기
- [ ] 신규모듈 : 알림?
    - db를 더 하드하게 써야하는 요구사항
    - 코루틴 효율이 좋은 요구사항
