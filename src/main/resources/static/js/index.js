const innerList = document.getElementById("inner_list");
const slides = document.querySelectorAll(".inner");
const prevBtn = document.getElementById("button_left");
const nextBtn = document.getElementById("button_right");

let index = 0;

function showSlide(n) {
  const slideWidth = document.getElementById("mbti_contents").clientWidth;
  index = (n + slides.length) % slides.length;
  innerList.style.transform = `translateX(-${index * slideWidth}px)`;
}

prevBtn.addEventListener("click", () => showSlide(index - 1));
nextBtn.addEventListener("click", () => showSlide(index + 1));

// 자동 슬라이드

// 창 크기 바뀔 때 현재 위치 유지
window.addEventListener("resize", () => showSlide(index));

// 페이지 로드 시 첫 슬라이드 표시
window.addEventListener("load", () => showSlide(0));

//mbti 랭킹 클릭 시 글씨 바뀜
const mbtiRankBtns = document.querySelectorAll("#mbti_list button");
// 처음 로드 시 E 버튼만 선택되도록
document.addEventListener("DOMContentLoaded", () => {
  mbtiRankBtns.forEach((b) => b.classList.remove("active"));
  const firstBtn = document.querySelector("#mbti_list button"); // 첫 번째 버튼(E)
  if (firstBtn) {
    firstBtn.classList.add("active");
  }
});
mbtiRankBtns.forEach((btn) => {
  btn.addEventListener("click", () => {
    // 다른 버튼들에서 active 제거
    mbtiRankBtns.forEach((b) => b.classList.remove("active"));
    // 클릭한 버튼만 active 추가
    btn.classList.add("active");
  });
});
