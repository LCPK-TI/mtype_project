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


	function addCardEventListeners(card) {
		const qtySpan = card.querySelector(".qty");
		const priceSpan = card.querySelector(".price");
		//이 카드의 단가와 재고 결정
		const skuNo = card.dataset.skuNo; //옵션카드는 skuNo가 있고, 단일 상품은 없음
		const sku = skuNo ? serverData.skus.find(s => s.skuNo == skuNo) : null;

		//상품 기본가
		const unitPrice = serverData.basePrice;
		//sku 재고가 있으면 쓰고, 없으면 단일 상품의 재고 사용
		const stock = sku ? sku.stock : (serverData.skus.length > 0 ? serverData.skus[0].stock : 999); //단일 상품 serverData.skus배열에 데이터가 한개있으면 해당 데이터 stock사용, 예외적으로 없다면 에러방지용으로 999 넣기


		//카드 내부의 가격을 실시간으로 업데이트하는 함수
		function updateCardPrice() {
			const quantity = parseInt(qtySpan.textContent);
			const totalPriceForCard = unitPrice * quantity;
			priceSpan.textContent = totalPriceForCard.toLocaleString() + "원";
		}
		card.querySelector(".plus").addEventListener("click", () => {
			let qty = parseInt(qtySpan.textContent);
			if (qty < stock) {
				qtySpan.textContent = qty + 1;
				updateTotalPrice(); //페이지 전체 총합 업데이트 
				updateCardPrice(); //카드 내부 가격 업데이트
			} else {
				alert(`최대 구매 가능 수량은 ${stock}개입니다.`);
			}
		});
		card.querySelector(".minus").addEventListener("click", () => {
			let qty = parseInt(qtySpan.textContent);
			if (qty > 1) {
				qtySpan.textContent = qty - 1;
				updateTotalPrice();//페이지 전체 총합 업데이트
				updateCardPrice();  //카드 내부 가격 업데이트
			}
		});
		const removeBtn = document.querySelector(".remove_btn");
		if (removeBtn) {
			card.querySelector(".remove_btn").addEventListener("click", () => {
				card.remove();
				updateTotalPrice(); //페이지 전체 총합 업데이트
			});
		}
	}


	//페이지 하단의 총 가격 업데이하는 함수
	function updateTotalPrice() {
		let total = 0;
		document.querySelectorAll(".selected_card, #single_product_card").forEach(card => {
			const skuNo = card.dataset.skuNo;
			const sku = skuNo ? serverData.skus.find(s => s.skuNo == skuNo) : null;
			const price = serverData.basePrice;
			const qty = parseInt(card.querySelector(".qty").textContent);
			total += price * qty;
		});
		const totalEl = document.getElementById("total_price_amount");
		if (totalEl) totalEl.textContent = total.toLocaleString() + "원";
	}
	//옵션이 없는 상품 처리
	const singleProductCard = document.getElementById("single_product_card");
	if (singleProductCard) {
		// 단일 상품 카드에 수량 조절 이벤트 리스너를 바로 추가
		addCardEventListeners(singleProductCard);
		// 초기 총 가격을 설정
		updateTotalPrice();
	}
	// 옵션이 있는 상품 처리
	if (serverData.optionGroups && serverData.optionGroups.length > 0) {

		const optionSelects = document.querySelectorAll(".option_select");
		const selectedContainer = document.querySelector(".selected_product");

		// 첫 번째 옵션 외에는 비활성화
		optionSelects.forEach((s, i) => { if (i > 0) s.disabled = true; });

		optionSelects.forEach((select, index) => {
			select.addEventListener("change", () => {
				// 현재까지 선택된 옵션 ID 목록 (선택 안 된 값은 제외)
				const selectedNosSoFar = Array.from(optionSelects)
					.slice(0, index + 1)
					.map(s => s.value)
					.filter(Boolean) // null, undefined, "" 등 falsy 값 제거
					.map(Number);

				// 다음 select 활성화 및 옵션 필터링
				const nextSelect = optionSelects[index + 1];
				if (nextSelect) {
					// 현재 select가 선택되지 않았다면, 다음 select를 비활성화하고 초기화
					if (!select.value) {
						nextSelect.value = "";
						nextSelect.disabled = true;
						return;
					}

					nextSelect.disabled = false;
					nextSelect.value = ""; // 다음 select 선택값 초기화

					// 다음 select의 각 option 활성화/비활성화 처리
					Array.from(nextSelect.options).forEach(opt => {
						if (!opt.value) return; // placeholder 옵션("-- 선택 --")은 무시

						// 테스트할 옵션 조합
						const testNos = [...selectedNosSoFar, Number(opt.value)];
						// 해당 조합으로 구매 가능한 재고가 있는지 확인
						opt.disabled = !hasStockForCombination(testNos);
					});
				}

				// 모든 select가 선택되었는지 확인
				const allSelected = Array.from(optionSelects).every(s => s.value);
				if (allSelected) {
					const selectedOptionNos = Array.from(optionSelects).map(s => Number(s.value));
					const foundSku = findSkuByOptions(selectedOptionNos);

					if (foundSku) {
						createSelectedCard(foundSku);
					}

					// 모든 select를 초기 상태로 리셋
					optionSelects.forEach((s, i) => {
						s.value = "";
						if (i > 0) {
							s.disabled = true;
							// 다음 옵션들의 disabled 상태도 모두 풀어줌
							Array.from(s.options).forEach(o => o.disabled = false);
						}
					});
				}
			});
		});

		function hasStockForCombination(selectedNos) {
			for (const sku of serverData.skus) {
				const skuSet = new Set(sku.optionNos);
				if (sku.stock > 0 && selectedNos.every(No => skuSet.has(No))) {
					return true;
				}
			}
			return false;
		}

		function findSkuByOptions(selectedNos) {
			selectedNos.sort((a, b) => a - b);
			return serverData.skus.find(sku => {
				const sortedNos = [...sku.optionNos].sort((a, b) => a - b);
				return JSON.stringify(sortedNos) === JSON.stringify(selectedNos);
			});
		}

		function createSelectedCard(sku) {
			if (selectedContainer.querySelector(`[data-sku-no="${sku.skuNo}"]`)) {
				alert("이미 선택된 옵션입니다.");
				return;
			}

			const optionText = Array.from(optionSelects)
				.map(s => s.options[s.selectedIndex]?.text || "")
				.join(" / ");

			const card = document.createElement("div");
			card.className = "selected_card";
			card.dataset.skuNo = sku.skuNo;
			card.innerHTML = `
		            <div class="selected_header">
		                <span>${optionText}</span>
		                <button class="remove_btn">X</button>
		            </div>
					<div class="selected_body">
						<div class="price_quantity">
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
			            <span class="price">${serverData.basePrice.toLocaleString()}원</span>
					</div>
		        `;
			selectedContainer.appendChild(card);

			addCardEventListeners(card, sku);
			updateTotalPrice();
		}


	}


	//사진 후보 클릭하여 사진 크게 띄우기
	const thumbnail = document.getElementById("thumbnail");
	const bottomImgs = document.querySelectorAll(".bottom_imgs img");

	bottomImgs.forEach(img => {
		img.addEventListener("click", () => {
			//클릭한 이미지의 src를 대표이미지에 적용
			thumbnail.src = img.src;

		})
	});

	//상세 사진 접었다 펴기
	function moreDetail() {
		document.querySelector(".detail_content").classList.remove("hidden");
		document.querySelector(".detailMore").remove(); // 버튼 제거
	}
	document.getElementById("btnMore").addEventListener("click", moreDetail);
});
