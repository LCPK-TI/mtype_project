document.addEventListener("DOMContentLoaded", () => {
	// 최근 본 상품
	// 토큰 가져오기(로그인)
	if (window.isUserLoggedIn) {

		//로그인 상태일 때만, 최근 본 상품 저장을 실행
		const pathParts = window.location.pathname.split('/');
		const productNO = pathParts[pathParts.length - 1];

		if (productNO && !isNaN(productNO)) {
			fetch(`/api/recent-views/${productNO}`, {
				method: "POST"
			})
				.then(response => {
					if (response.ok) {
						console.log(`상품번호 ${productNO} 조회 기록 저장 성공`);
					} else {
						console.error("조회 기록 저장 실패. 상태: ", response.status);
					}
				})
				.catch(error => {
					console.error("조회 기록 저장 중 네트워크 오류 발생", error);
				});
		}
	} else {
		// 로그아웃 상태일 경우
		console.log("로그인하지 않은 사용자. 최근 본 상품을 저장하지 않습니다.");
	}


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

	//수량 추가시 가격 변경
	function addCardEventListeners() {
		const stock = serverData.stock;
		const qtySpan = document.querySelector(".qty");

		document.querySelector(".plus").addEventListener("click", () => {
			let qty = parseInt(qtySpan.textContent);
			if (qty < stock) {
				qtySpan.textContent = qty + 1;
				updateTotalPrice();
			} else {
				alert(`최대 구매 가능 수량은 ${stock}개입니다.`);
			}
		});

		document.querySelector(".minus").addEventListener("click", () => {
			let qty = parseInt(qtySpan.textContent);
			if (qty > 1) {
				qtySpan.textContent = qty - 1;
				updateTotalPrice();
			}
		});
	}
	//페이지 하단의 총 가격 업데이하는 함수
	function updateTotalPrice() {
		let total = 0;
		const price = serverData.price;
		const qty = parseInt(document.querySelector(".qty").textContent);
		total += price * qty;
		const totalEl = document.getElementById("total_price_amount");
		if (totalEl) totalEl.textContent = total.toLocaleString() + "원";
	}
	// 초기 실행
	addCardEventListeners();


	//상세정보 더보기 
});
