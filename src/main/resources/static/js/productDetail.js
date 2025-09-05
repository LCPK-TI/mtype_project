document.addEventListener("DOMContentLoaded", () => {
  //장바구니 담기 성공
  const cartBtn = document.getElementById("cart_btn");
  const closeCart = document.getElementsByClassName("cancel")[0];
  const closeGiftCart = document.getElementsByClassName("cancel")[1];

  function putCart() {
    document.getElementById("cart_modal").style.display = "block";
    document.body.classList.add("modal-open"); //모달 open 시 스크롤 막기 위해 body에 class 추가
  }
  function closeModal() {
    document.getElementById("cart_modal").style.display = "none";
    document.getElementById("giftcart_modal").style.display = "none";
    document.body.classList.remove("modal-open");
  }

  cartBtn.addEventListener("click", putCart);
  closeCart.addEventListener("click", closeModal);

  //옵션 미선택 시 장바구니,구매,선물 실패
  // const ok = document.getElementById("ok");
  function closeFailed() {
    document.getElementById("failed_cart").style.display = "none";
    document.body.classList.remove("modal-open");
  }
  function failedCart() {
    document.getElementById("failed_cart").style.display = "block";
    document.body.classList.add("modal-open");
  }
  // cartBtn.addEventListener("click", failedCart);
  // ok.addEventListener("click", closeFailed);

  //선물하기
  const giftBtn = document.getElementById("gift_btn");
  const giftCartBtn = document.getElementById("cart_gift");
  function toGift() {
    document.getElementById("gift_modal").style.display = "block";
  }
  function putGiftCart() {
    document.getElementById("gift_modal").style.display = "none";
    document.getElementById("giftcart_modal").style.display = "block";
    document.body.classList.add("modal-open");
  }

  giftBtn.addEventListener("click", toGift);
  giftCartBtn.addEventListener("click", putGiftCart);
  closeGiftCart.addEventListener("click", closeModal);

  //문의하기
  const inquiryBtn = document.getElementById("inquiry_btn");
  const cancelBtn = document.getElementById("cancel_inquiry");
  const submit = document.getElementById("register_inquiry");
  function openInquiry() {
    document.getElementById("write_inquiry").style.display = "block";
    document.body.classList.add("modal-open");
  }
  function cancelWrite() {
    document.getElementById("write_inquiry").style.display = "none";
    document.body.classList.remove("modal-open");
  }
  function submitInquiry() {
    document.getElementById("write_inquiry").style.display = "none";
  }
  inquiryBtn.addEventListener("click", openInquiry);
  cancelBtn.addEventListener("click", cancelWrite);
});
