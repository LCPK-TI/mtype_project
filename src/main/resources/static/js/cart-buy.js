/* 선물 장바구니에 담기 (모달) */
const modal = document.getElementById("giftModal");
const openBtn = document.querySelector(".add-to-cart");

openBtn.addEventListener("click", () => {
  modal.style.display = "block";
});

window.addEventListener("click", (e) => {
  if (e.target === modal) {
    modal.style.display = "none";
  }
});

/* 전체 선택/해제 */
const selectAll = document.getElementById("select-all");
const productCheckboxes = document.querySelectorAll(".product-checkbox");

// 전체 선택 클릭 시 → 모든 상품 체크박스 변경
selectAll.addEventListener("change", () => {
  productCheckboxes.forEach((cb) => {
    cb.checked = selectAll.checked;
  });
});

// 개별 체크박스 클릭 시 → 전체 선택 체크박스 상태 업데이트 (전체 선택 체크박스 해제)
productCheckboxes.forEach((cb) => {
  cb.addEventListener("change", () => {
    const allChecked = Array.from(productCheckboxes).every((c) => c.checked);
    selectAll.checked = allChecked;
  });
});
