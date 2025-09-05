(function () {
  const modal = document.getElementById("rv_write_modal");
  if (!modal) return; 

  const openBtns = document.querySelectorAll(".js_open_review_modal");
  const overlay  = modal.querySelector(".rv_overlay");
  const closeEls = modal.querySelectorAll("[data-close]");

  const stars    = modal.querySelectorAll(".rv_star");
  const rating   = document.getElementById("rv_rating");
  const textarea = document.getElementById("rv_text");
  const counter  = document.getElementById("rv_count");
  const submit   = document.getElementById("rv_submit");

  let lastFocus = null;

  function openModal() {
    lastFocus = document.activeElement;
    modal.classList.add("is_open");
    (stars[0] || textarea)?.focus();
    document.addEventListener("keydown", onKey);
    updateCount();
  }

  function closeModal() {
    modal.classList.remove("is_open");
    document.removeEventListener("keydown", onKey);
    if (lastFocus) lastFocus.focus();
  }

  function onKey(e) {
    if (e.key === "Escape") closeModal();
  }

  /* 별점 */
  stars.forEach((s) => {
    s.addEventListener("click", () => {
      const v = Number(s.dataset.value);
      rating.value = v;
      stars.forEach((t) => t.classList.toggle("on", Number(t.dataset.value) <= v));
    });
  });

  /* 글자 수 카운트 */
  function updateCount() {
    counter.textContent = `${textarea.value.length} / ${textarea.maxLength}`;
  }
  textarea.addEventListener("input", updateCount);

  /* 열기,닫기 */
  openBtns.forEach((b) => b.addEventListener("click", openModal));
  overlay.addEventListener("click", closeModal);
  closeEls.forEach((b) => b.addEventListener("click", closeModal));

  /* 등록 */
  submit.addEventListener("click", () => {
    closeModal();
  });
})();
