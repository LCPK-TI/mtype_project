document.addEventListener("DOMContentLoaded", () => {
  const stepKeys = [
    "결제완료",
    "상품준비중",
    "배송준비중",
    "배송중",
    "배송완료",
  ];
  const counts = Object.fromEntries(stepKeys.map((k) => [k, 0]));

  document.querySelectorAll(".order_item").forEach((item) => {
    let status = (item.getAttribute("data-status") || "").trim();
    const tracking = item.getAttribute("data-tracking");
    const statusBox = item.querySelector(".item_status");

    // 구매확정도 배송완료로 카운트
    if (status === "구매확정") status = "배송완료";
    if (counts.hasOwnProperty(status)) counts[status]++;

    // 배송중/배송완료일 때 운송장번호 출력
    if (
      (status === "배송중" ||
        status === "배송완료" ||
        status === "배송준비중") &&
      tracking
    ) {
      const trackingEl = document.createElement("div");
      trackingEl.className = "delivery_no";
      trackingEl.textContent = `${tracking}`;
      statusBox.appendChild(trackingEl);
    }
  });

  // 상단 진행상태에 해당 진행 갯수
  document.querySelectorAll(".status_steps .step").forEach((step) => {
    const key = step.getAttribute("data-step");
    const num = counts[key] || 0;
    const countEl = step.querySelector(".count");
    if (countEl) countEl.textContent = String(num);
    step.classList.toggle("has-count", num > 0);
  });
});
