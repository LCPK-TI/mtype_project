document.addEventListener("DOMContentLoaded", function() {

	/*카테고리*/
	const cateBtn = document.getElementById("cate_btn");
	const cateModal = document.getElementById("show_categories");
	const mainCategoryItems = document.querySelectorAll('#main_category li');
	const subCategoryLists = document.querySelectorAll('#sub_category_wrapper .sub_category_list');

	function activateCategory(selectedItem) {
		// 1. 모든 메뉴를 일단 비활성화 상태로 초기화
		mainCategoryItems.forEach(i => i.classList.remove('active'));
		subCategoryLists.forEach(list => list.classList.remove('active'));

		// 2. 선택된 메인 카테고리가 있다면 활성화
		if (selectedItem) {
			selectedItem.classList.add('active');

			const categoryNo = selectedItem.dataset.categoryNo;
			const correspondingSubList = document.querySelector(`.sub_category_list[data-parent-no="${categoryNo}"]`);

			// 3. 해당하는 서브 카테고리 목록이 있으면 활성화
			if (correspondingSubList) {
				correspondingSubList.classList.add('active');
			}
		}
	}

	// 카테고리 버튼 클릭 시 모달을 열고 첫 번째 메뉴를 활성화하는 함수
	function showCategory(event) {
		cateModal.style.display = 'block';

		// 첫 번째 메인 카테고리 아이템을 찾습니다.
		const firstMainCategory = document.querySelector('#main_category li:first-child');

		// 첫 번째 아이템을 활성화합니다.
		activateCategory(firstMainCategory);
	}

	// 카테고리 버튼에 클릭 이벤트 연결
	cateBtn.addEventListener('click', showCategory);

	// 각 메인 카테고리 항목에 마우스 오버 이벤트 연결
	mainCategoryItems.forEach(item => {
		item.addEventListener('mouseover', () => {
			// 마우스를 올린 아이템을 활성화합니다.
			activateCategory(item);
		});
	});

	/*검색창 */
	const searchInput = document.getElementById("search");
	const searchBtn = document.getElementById("search_btn");
	const searchModal = document.getElementById("show_search");
	const closeSearchBtn = document.getElementById("search_footer");
	const popularSearchContainer = document.getElementById("popular_search");
	const recentSearchContainer = document.getElementById("recent_search");

	// 검색창 열때마다 서버에서 검색어 데이터 불러옴
	async function loadSearchKeywords() {
		fetch('/api/search/popular')
			.then(res => res.json())
			.then(keywords => {
				popularSearchContainer.innerHTML = '<div id="popular_header"><b>인기 검색어</b></div>';
				keywords.forEach((keyword, index) => {
					const rankDiv = document.createElement('div');
					rankDiv.className = 'rank_search';
					rankDiv.innerHTML = `<span>${index + 1}</span><span>${keyword}</span>`;
					popularSearchContainer.appendChild(rankDiv);
				});
			});
		// 최근 검색어
		if (window.isUserLoggedIn) {
			fetch('/api/search/recent')
				.then(res => res.json())
				.then(keywords => {
					recentSearchContainer.innerHTML = '<div id="search_header"><b>최근 검색어</b><button>전체 삭제</button></div>';
					keywords.forEach(keyword => {
						const contentDiv = document.createElement('div');
						contentDiv.className = 'search_content';
						contentDiv.innerHTML = `<span>${keyword}</span><button>X</button>`;
						recentSearchContainer.appendChild(contentDiv);
					});
				});
		} else {
			// 로그인 아닌 경우
			recentSearchContainer.innerHTML = '<div id="search_header"><b>최근 검색어</b></div>' +
				'<div class="search_content" style="color:gray; font-size:13px;">로그인 후 이용 가능합니다.</div>';
		}
	}
	// 검색 버튼 또는 엔터를 눌렀을 때.
	function executeSearch() {
		const keyword = searchInput.value.trim();
		if (keyword) {
			// 검색어가 존재하는 경우
			fetch('/api/search', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ keyword: keyword })
			}).then(response => {
				if (response.ok) {
					window.location.href = `/product/search?query=${encodeURIComponent(keyword)}`;
				}
			});
		}
	}
	// 검색 창 클릭 시
	searchInput.addEventListener("click", function() {
		searchModal.style.display = "block";
		document.body.classList.add("modal-open");
		loadSearchKeywords();
	});

	// 닫기 버튼
	closeSearchBtn.addEventListener("click", function() {
		searchModal.style.display = "none";
		document.body.classList.remove("modal-open");
	});

	// 검색 버튼 클릭
	searchBtn.addEventListener('click', executeSearch);
	// 검색창에서 엔터 키
	searchInput.addEventListener('keydown', function(event) {
		if (event.key === 'Enter') {
			executeSearch();
		}
	});

	// 마이페이지 아이콘
	const mypageIcon = document.getElementById('mypage-icon');
	if (mypageIcon) {
		mypageIcon.addEventListener('click', function() {
			if (window.isUserLoggedIn) {
				window.location.href = '/user/mypage';
			} else {
				alert('로그인이 필요합니다.');
				window.location.href = '/user/login';
			}
		});
	}

	// 로그인 버튼
	const loginBtn = document.getElementById('login-btn');
	if (loginBtn) {
		loginBtn.addEventListener('click', function() {
			window.location.href = '/user/login';
		});
	}

	// 로그아웃 버튼
	const logoutBtn = document.getElementById('logout-btn');
	if (logoutBtn) {
		logoutBtn.addEventListener('click', function() {
			window.location.href = '/user/logout';
		});
	}

	/*기념일 클릭시 모달 */
	const prvAnv = document.getElementById("preview_anniversary");
	const anvModal = document.getElementById("anniversary_modal");
	const img = document.getElementById("anv_btn");
	let isOpen = false;

	prvAnv.addEventListener("click", function() {
		isOpen = !isOpen;
		anvModal.style.display = isOpen ? "block" : "none";
		img.src = isOpen ? "/img/page_up.png" : "/img/down.png";
	});


	// 외부 클릭시 모달 닫히게
	document.addEventListener("click", function(event) {
		if (!cateModal.contains(event.target) && !cateBtn.contains(event.target)) {
			cateModal.style.display = "none";
		}
		if (!searchModal.contains(event.target) && !openSearchBtn.contains(event.target)) {
			searchModal.style.display = "none";
		}
		if (!anvModal.contains(event.target) && !prvAnv.contains(event.target)) {
			anvModal.style.display = "none";
		}
	});

});

