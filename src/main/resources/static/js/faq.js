/* 토글기능 */
document.addEventListener("DOMContentLoaded", function () {
  const titles = document.querySelectorAll(".faq-title");
  const arrows = document.querySelectorAll(".arrow");
  const contents = document.querySelectorAll(".faq-content");

  function toggleContent(index) {
    const isOpen = contents[index].style.display === "block";

    // 모두 닫기
    contents.forEach((c) => (c.style.display = "none"));
    arrows.forEach((a) => (a.textContent = "▼")); // 모든 화살표 초기화

    if (!isOpen) {
      contents[index].style.display = "block";
      arrows[index].textContent = "▲"; // 클릭한 항목만 ▲로 변경
    }
  }

  titles.forEach((title, index) => {
    title.addEventListener("click", () => toggleContent(index));
  });

  arrows.forEach((arrow, index) => {
    arrow.addEventListener("click", () => toggleContent(index));
  });
});

/* 탭 메뉴 */
const buttons = document.querySelectorAll(".tab");
const title = document.getElementById("title");

buttons.forEach((button) => {
  button.addEventListener("click", () => {
    // 모든 버튼에서 active 제거
    buttons.forEach((btn) => btn.classList.remove("active"));
    // 클릭한 버튼에 active 추가
    button.classList.add("active");
    // 제목 변경
    title.textContent = button.textContent;
  });
});
