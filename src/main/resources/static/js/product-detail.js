document.addEventListener("DOMContentLoaded", () => {
	// 최근 본 상품
	// 토큰 가져오기(로그인)
	const jwtToken = localStorage.getItem("jwt");
			
			// 토큰 없으면 로그인 안한 사용자. => 최근 본 상품 저장 X
			if(!jwtToken){
				console.log("로그인 하지 않은 사용자. 최근 본 상품 저장 X");
				return;
			}
			
			// url 경로를 쪼개서 상품 번호 가져옴.
			const pathParts = window.location.pathname.split('/');
			const productNO = pathParts[pathParts.length-1];	// 배열 마지막 요소가 상품 번호
			
			if(productNO && !isNaN(productNO)){
				// 상품 번호가 실존하고, 숫자이면
				fetch(`/api/recent-views/${productNO}`, {
					method: "POST",
					headers: {
						"Authorization": "Bearer " + jwtToken,
					},
				})
				.then(response =>{
					if(response.ok){
						console.log(`상품번호 ${productNO} 조회 기록 저장 성공`);
					}else{
						console.error("조회 기록 저장 실패.. 상태: ", response.status);
					}
				})
				.catch(error => {
					console.error("조회 기록 저장 중 네트워크 오류 발생", error);
				});
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

	//옵션 클릭시 추가
	const optionSelect = document.getElementById("options");
	const selectedContainer = document.querySelector(".selected_product");

	optionSelect.addEventListener("change", () => {
		const selectedOption = optionSelect.value;
		if (!selectedOption) return;

		//이미 선택된 옵션인지 체크
		const existing = selectedContainer.querySelector(`[data-option='${selectedOption}']`);
		if (existing) {
			alert("이미 선택된 옵션");
			return;
		}

		//새로운 선택 상품 요소 생성
		const div = document.createElement("div");
		div.classList.add("selected_product");
		div.dataset.option = selectedOption;
		div.innerHTML = `
		<div class="selected_card">
			<div class="selected_header">
				<span>${selectedOption}</span>
				<button class="remove_btn">X</button>
			</div>
			<div class="price_quantity">
				<p class="price">가격</p>
				<div class="quantity">
					<table>
						<tr>
							<td><button class="minus">-</button></td>
							<td><span class="qty">1</span></td>
							<td><button class="plus">+</button>
						</tr>
					</table>
				</div>
			</div>
		</div>
		`;
		selectedContainer.appendChild(div);

		//옵션 삭제
		div.querySelector(".remove_btn").addEventListener("click", () => {
			div.remove();
			updateTotalPrice();
		});

		//수량 증가/감소
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

		updateTotalPrice();
	});

	function updateTotalPrice() {
		let total = 0;
		selectedContainer.querySelectorAll(".selected_product").forEach(sp => {
			const price = 10000; //상품 가격
			const qty = parseInt(sq.querySelector(".qty").textContent);
			total += price * qty;
			sp.querySelector(".price").textContent = price.toLocaleString() + "원";
		});
		document.querySelector(".total_price b").textContent = total.toLocaleString() + "원";
	}
	
	//사진 후보 클릭하여 사진 크게 띄우기
	const thumbnail = document.getElementById("thumbnail");
	const bottomImgs = document.querySelectorAll(".bottom_imgs img");
	
	bottomImgs.forEach(img=>{
		img.addEventListener("click",()=>{
			//클릭한 이미지의 src를 대표이미지에 적용
			thumbnail.src = img.src;
			
		})
	})
});
