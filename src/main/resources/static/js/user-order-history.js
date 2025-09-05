document.querySelectorAll(".order_item").forEach((item) => {
  const status = item.querySelector(".item_status");
  const btn_ordered_list = item.querySelector(".item_btn");

  if (!status || !btn_ordered_list) return;

  const statusText = status.textContent.trim();

  //버튼에 저장된 텍스트 초기화
  btn_ordered_list.innerHTML = "";

  // 운송장번호
  const createDeliveryNo = (num) => {
    const delivery = document.createElement("div");
    delivery.className = "delivery_no";
    delivery.textContent = `${num}`;
    return delivery;
  };

  if (statusText == "상품준비중") {
    const cancelBtn = document.createElement("button");
    cancelBtn.textContent = "주문 취소";
    cancelBtn.addEventListener("click", () => {
      openModal(
        "주문을 취소하시겠습니까?<br>배송준비중 단계가 되면 취소할 수 없습니다.",
        () => {
          // 취소시 기능
        }
      );
    });
    btn_ordered_list.appendChild(cancelBtn);
  } else if (statusText == "배송완료") {
    const returnBtn = document.createElement("button");
    returnBtn.textContent = "반품 신청";
    returnBtn.addEventListener("click", () => {
      openModal("반품 신청하시겠습니까?", () => {
        // 반품시 기능
      });
    });
    btn_ordered_list.appendChild(returnBtn);
    //운송장
    status.appendChild(createDeliveryNo("1111-2222-3333"));
  } else {
    //버튼 없는 경우
    status.appendChild(createDeliveryNo("2222-3333-4567"));
  }
});

/* 모달 */
const modal = document.getElementById("confirmModal");
const modalMsg = document.getElementById("modalMsg");
const modalOk = document.getElementById("modalOk");
const modalNo = document.getElementById("modalNo");

let modalFn = null; //확인버튼 누르면 실행할 함수를 저장하기 위함

//열기 함수 정의
function openModal(msg, onOk) {
  modalMsg.innerHTML = msg;
  modal.style.display = "flex";
  modalFn = onOk;
}
//모달 닫기 함수 정의
function closeModal() {
  modal.style.display = "none";
  modalFn = null;
}
//모달에서 확인 버튼
modalOk.addEventListener("click", () => {
  if (modalFn) {
    // 확인 후 실행할 함수 있으면 실행
    modalFn();
  }
  closeModal();
});
// 취소 버튼
modalNo.addEventListener("click", closeModal);
