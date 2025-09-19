document.addEventListener("DOMContentLoaded", () => {
	let currentPage = initialPageNumber; //현재 불러온 페이지 번호
	let isLastPage = initialIsLast; //마지막 페이지인지 여부
	let isLoading = false; //중복 요청 방지 플래그
	let currentSort = "isbn,desc"; // 정렬 기준

	const productListContainer = document.querySelector(".product_list"); //불러온 상품들을 넣을 html 영역

	//비동기 요청으로 상품 데이터를 불러옴. clearExisting=ture면 기존 상품 목록을 지우고 새로 그림(정렬변경시)
	const fetchProducts = async (page, sort, clearExisting = false) => {
		//중복 요청 방지
		if (isLoading || (isLastPage && !clearExisting)) return; //이미 로딩중이면 실행안함. 마지막 페이지라면 추가 요청 안 함 (새로 요청할땐 제외)
		isLoading = true;

		// API URL을 동적으로 생성
		let apiUrl = `/api/books?page=${page}&sort=${sort}`;

		// currentCategoryNo 변수에 값이 있을 때만 URL에 categoryId 파라미터를 추가
		if (currentCategoryNo) {
			apiUrl += `&categoryNo=${currentCategoryNo}`;
		}

		try {
			//데이터 요청
			const response = await fetch(apiUrl);
			if (!response.ok) throw new Error("Server response not ok");
			const bookSlice = await response.json(); //서버에서 Slice<BookListDto> json 받아옴

			//기존 상품 지우기
			if (clearExisting) {
				productListContainer.innerHTML = '';
			}
			
			//상품 카드 생성 & 추가
			bookSlice.content.forEach(book => {
				const productCardHTML = createProductCardHTML(book);
				productListContainer.insertAdjacentHTML('beforeend', productCardHTML);
			});
			
			//상태 갱신
			currentPage = bookSlice.number;
			isLastPage = bookSlice.last;
		} catch (error) {
			console.error("상품을 불러오는 데 실패했습니다.", error);
		} finally {
			isLoading = false;
		}
	};
	//상품 카드 생성
	const createProductCardHTML = (book) => {
		const formattedPrice = book.price.toLocaleString('ko-KR');
		return `
            <div class="product_card">
                <a href="/book/${book.isbn}">
                    <div class="view_img">
                        <img class="product_img" src="${book.imgUrl}" alt="상품이미지" />
                        <button class="wishlist_btn">
                            <img src="/img/heart_empty.png" />
                        </button>
                    </div>
                    <div class="product_info">
                        <p class="product_name">${book.title}</p>
                        <p class="product_price">${formattedPrice}원</p>
                    </div>
                </a>
            </div>
        `;
	};

	//정렬 옵션 클릭시 실행
	const sortOptions = document.querySelectorAll(".sort-option");
	sortOptions.forEach(option => {
		option.addEventListener("click", (e) => {
			sortOptions.forEach(opt => opt.classList.remove("active")); //기존 active 제거 -> 클릭한 버튼에 active 추가
			e.currentTarget.classList.add("active");

			const newSort = e.currentTarget.dataset.sort; //새로운 정렬 기준(data-sort)을 읽어와서 적용
			if (currentSort !== newSort) {
				currentSort = newSort;
				currentPage = 0;
				isLastPage = false;
				fetchProducts(0, currentSort, true); 
			}
		});
	});

	//무한 스크롤
	const scrollTrigger = document.getElementById('scroll-trigger'); //scroll-trigger 요소가 뷰포트에 보이면 isIntersecting 다음 페이지 로드
	const observer = new IntersectionObserver((entries) => {
		if (entries[0].isIntersecting) {
			fetchProducts(currentPage + 1, currentSort);
		}
	}, { threshold: 0.1 }); //요소가 10% 이상 보일 때 실행

	if (!isLastPage) { //마지막 페이지가 아니면 관찰만
		observer.observe(scrollTrigger);
	}
});