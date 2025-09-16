document.addEventListener("DOMContentLoaded", () => {
	// 장바구니 담기 모달
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
		document.body.classList.add("modal-open");
	}
	function closeModal() {
		document.getElementById("cart_modal").style.display = "none";
		document.getElementById("giftcart_modal").style.display = "none";
		document.body.classList.remove("modal-open");
	}

	cartBtn.addEventListener("click", putCart);
	closeCart.addEventListener("click", closeModal);

	// 옵션 선택 시 아래에 추가
	const optionSelects = document.querySelectorAll(".option_select");
	const selectedContainer = document.querySelector(".selected_product");

	optionSelects.forEach(select => {
		select.addEventListener("change", () => {
			const selectedOptionNo = select.value;
			const selectedOptionText = select.options[select.selectedIndex].text;

			if (!selectedOptionNo) return;

			// 이미 선택된 옵션인지 체크
			const existing = selectedContainer.querySelector(`[data-option='${selectedOptionNo}']`);
			if (existing) {
				alert("이미 선택된 옵션입니다.");
				return;
			}

			// 새로운 선택 상품 요소 생성
			const div = document.createElement("div");
			div.classList.add("selected_card");
			div.dataset.option = selectedOptionNo;
			div.innerHTML = `
				<div class="selected_header">
					<span>${selectedOptionText}</span>
					<button class="remove_btn">X</button>
				</div>
				<div class="price_quantity">
					<p class="price">${serverData.basePrice.toLocaleString()}원</p>
					<div class="quantity">
						<table>
							<tr>
								<td><button class="minus">-</button></td>
								<td><span class="qty">1</span></td>
								<td><button class="plus">+</button></td>
							</tr>
						</table>
					</div>
				</div>
			`;

			selectedContainer.appendChild(div);

			// 옵션 삭제
			div.querySelector(".remove_btn").addEventListener("click", () => {
				div.remove();
				updateTotalPrice();
			});


			updateQuantity(div);
			updateTotalPrice();
		});
	});
	function updateQuantity(div) {
		// 수량 증가/감소
		const qtySpan = div.querySelector(".qty");
		div.querySelector(".plus").addEventListener("click", () => {
			qtySpan.textContent = parseInt(qtySpan.textContent) + 1;
			updateTotalPrice();
		});
		div.querySelector(".minus").addEventListener("click", () => {
			let qty = parseInt(qtySpan.textContent);
			if (qty > 1) qtySpan.textContent = qty - 1;
			updateTotalPrice();
		});
	}
	const singleCard = document.getElementById("single_product_card"); // 옵션 없는 상품 영역
	if (singleCard){
		updateQuantity(singleCard);
		updateTotalPrice();
	} 
	
	// 총 가격 업데이트
	function updateTotalPrice() {
		let total = 0;
		document.querySelectorAll(".selected_card, #single_product_card").forEach(card => {
			const price = serverData.basePrice; // 상품 기본 가격
			const qty = parseInt(card.querySelector(".qty").textContent);
			total += price * qty;
			card.querySelector(".price").textContent = (price * qty).toLocaleString() + "원";
		});
		document.getElementById("total_price_amount").textContent = total.toLocaleString() + "원";
	}

	// 썸네일 클릭 시 대표 이미지 교체
	const thumbnail = document.getElementById("thumbnail");
	const bottomImgs = document.querySelectorAll(".bottom_imgs img");

	bottomImgs.forEach(img => {
		img.addEventListener("click", () => {
			thumbnail.src = img.src;
		});
	});

	//상세정보 더보기 
	function moreDetail() {
		//이미지의 부모 div에서 'hidden' 클래스 제거
		document.querySelector(".detail_content").classList.remove("hidden");

		//더보기 버튼 제거
		document.querySelector(".detailMore").remove();
	}
	document.getElementById("btnMore").addEventListener("click", moreDetail);
});