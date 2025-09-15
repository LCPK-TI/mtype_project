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
	const closeSearchBtn = document.getElementById("search_footer");
	const searchModal = document.getElementById("show_search");
	const openSearchBtn = document.getElementById("search");

	function showSearch() {
		searchModal.style.display = "block";
		document.body.classList.add("modal-open");
	}

	function closeSearch() {
		document.getElementById("show_search").style.display = "none";
		document.body.classList.remove("modal-open");
	}

	openSearchBtn.addEventListener("click", showSearch);
	closeSearchBtn.addEventListener("click", closeSearch);


	const jwtToken = localStorage.getItem('jwt');
	const loginLogoutImg = document.getElementById('login-logout-img');

	if (loginLogoutImg) {
		if (jwtToken) {
			// 로그인 상태
			loginLogoutImg.src = "/img/logout.png";
			// 로그아웃
			loginLogoutImg.addEventListener('click', function() {

				localStorage.removeItem('jwt');
				window.location.href = '/';

			});

		} else {
			// 로그아웃 상태
			loginLogoutImg.src = "/img/login.png";

			loginLogoutImg.addEventListener('click', function() {
				window.location.href = '/user/login';
			});
		}
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