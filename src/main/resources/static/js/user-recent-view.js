document.addEventListener("DOMContentLoaded", function(){
	const jwtToken = localStorage.getItem("jwt");
	const productsGrid = document.querySelector(".products_grid");
	
	if(!jwtToken || !productsGrid){
		productsGrid.innerHTML = '<p class="login_required">로그인 후 이용하세요</p>';
		return;
	}
	
	fetch("/api/recent-views", {
		method: "GET",
		headers: {"Authorization": "Bearer " + jwtToken},
	})
	.then(response => {
		if(!response.ok) {throw new Error("데이터 로딩 실패");}
		return response.json();
	})
	.then(products => {
		productsGrid.innerHTML = "";
		
		if(products.length === 0){
			productsGrid.innerHTML = '<p class="no_items">최근 본 상품이 없습니다.</p>';
			return;
		}
		
		products.forEach(product => {
			const productCard = document.createElement("div");
			productCard.className = "product_card";
			const formattedPrice = new Intl.NumberFormat('ko-KR').format(product.productPrice);
			
			productCard.innerHTML = `
				<div class="product_img">
					<a href="/products/${product.productNo}">
						<img src="${product.thumbnailUrl}" alt="${product.productName}" />
					</a>
					<button class="wishlist_btn"><img src="/img/heart_empty.png" /></button>
				</div>
				<div class="product_info">
					<p class="store_name">${product.storeName}</p>
					<a href="/products/${product.productNo}">
						<p class="product_name">${product.productName}</p>
					</a>
					<p class="product_price">${formattedPrice}원</p>
				</div>
			`;
			productsGrid.appendChild(productCard);
		});
	})
	.catch(error => {
		
		console.error("최근 본 상품 조회 오류: ", error);
		productsGrid.innerHTML = '<p class="error_message">상품 로딩 중 오류가 발생했습니다.</p>';
	});
});