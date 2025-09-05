document.addEventListener("DOMContentLoaded", function () {
  const titles = document.querySelectorAll(".notice-title");
  const arrows = document.querySelectorAll(".arrow");
  const contents = document.querySelectorAll(".notice-content");

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
