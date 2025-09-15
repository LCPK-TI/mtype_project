document.getElementById("mbtiForm").addEventListener("submit", function (e) {
  e.preventDefault(); // 폼 제출 막기 (페이지 새로고침 방지)

  // 실제 결과 예시
  const mbti = "INFP";
  document.getElementById("mbtiResult").textContent = mbti;

  // 결과창 보이기
  document.getElementById("result").style.display = "block";
});
