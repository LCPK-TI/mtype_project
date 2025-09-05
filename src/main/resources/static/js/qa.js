document.addEventListener("click", (e) => {
  const btn = e.target.closest(".qa_summary");
  if (!btn) return;

  const item = btn.closest(".qa_item");
  const detail = item.querySelector(".qa_detail");
  const expanded = btn.getAttribute("aria-expanded") === "true";

  btn.setAttribute("aria-expanded", String(!expanded));
  detail.hidden = expanded;
});

/* 질문 삭제 */
document.addEventListener("click", (e) => {
  const del = e.target.closest(".js_delete");
  if (!del) return;

  const item = del.closest(".qa_item");
  if (!item) return;

  if (confirm("문의를 삭제할까요?")) {
    item.remove();
  }
});

(function () {
  const modal = document.getElementById("qa_edit_modal");
  if (!modal) return; 

  const overlay = modal.querySelector(".qa_modal_overlay");
  const closeEls = modal.querySelectorAll("[data-close]");
  const textarea = document.getElementById("qa_edit_text");
  const counter = document.getElementById("qa_edit_count");
  const saveBtn = document.getElementById("qa_edit_save");

  let lastFocus = null;
  let targetItem = null;   
  let targetTextEl = null; 

  function openModal(fromItem) {
    targetItem = fromItem;
    targetTextEl = targetItem.querySelector(".qa_text");

    textarea.value = (targetTextEl?.textContent || "").trim();
    updateCount();

    lastFocus = document.activeElement;
    modal.classList.add("is_open");
    textarea.focus();

    document.addEventListener("keydown", onKey);
  }

  function closeModal() {
    modal.classList.remove("is_open");
    document.removeEventListener("keydown", onKey);
    if (lastFocus) lastFocus.focus();
    targetItem = null;
    targetTextEl = null;
  }

  function onKey(e) {
    if (e.key === "Escape") closeModal();
  }

  function updateCount() {
    counter.textContent = `${textarea.value.length} / ${textarea.maxLength}`;
  }

  /* 입력 글자수 */
  textarea.addEventListener("input", updateCount);

  /* 닫기 */
  overlay.addEventListener("click", closeModal);
  closeEls.forEach((el) => el.addEventListener("click", closeModal));

  /* 수정하기 */
  document.addEventListener("click", (e) => {
    const editBtn = e.target.closest(".js_edit");
    if (!editBtn) return;

    const item = editBtn.closest(".qa_item");
    if (!item) return;

    openModal(item);
  });

  /*  질문 본문 변경 */
  saveBtn.addEventListener("click", () => {
    if (targetTextEl) {
      targetTextEl.textContent = textarea.value.trim();
    }
    closeModal();
  });
})();
