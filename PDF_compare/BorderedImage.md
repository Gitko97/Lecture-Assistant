# BorderedImage Class
1.  setBufferedImage
  - 비교할 이미지 두개를 받아서 초기화 한다.
2. setSearch
  - 차이가 있는 곳의 좌표를 구하기 위해 변수들을 초기화한다.
3. getSeartchStart, getSearchEnd
  - 추출할 이미지의 왼쪽 위 모서리 좌표를 찾는다.  
    두 이미지의 RGB값을 이용해서 찾으므로 노이즈가 발생한 부분까지 포함될 수 있음.  
    이를 방지하기 위해 countX와 countY 변수를 이용해서 5픽셀이 넘어가는 부분만 차이로 간주  
 4. extract
  - 이미지를 추출함
