document.addEventListener("DOMContentLoaded", function() {
	/*카테고리*/
	const cateBtn = document.getElementById("cate_btn");
	const cateModal = document.getElementById("show_categories");
	const mainItems = document.querySelectorAll("#main_category li");
	const subItems = document.querySelectorAll("#sub_category li");

	function showCategory() {
		cateModal.style.display = "block";
		//모달 open 시 스크롤 막기 위해 body에 class 추가
		document.body.classList.add("modal-open");
		//모달 열릴 때 첫번째 부모 카테고리의 서브 보여주기
		const defaultTarget = "outer";

		subItems.forEach((sub) => {
			sub.style.display =
				sub.getAttribute("data-parent") === defaultTarget ? "block" : "none";
		});

		// 메인 아이템 배경 초기화 후 "첫번째 부모 카테고리" 강조
		mainItems.forEach((i) => (i.style.backgroundColor = ""));
		const defaultItem = document.querySelector(
			'#main_category li[data-sub="outer"]'
		);
		if (defaultItem) {
			defaultItem.style.backgroundColor = "#f2f2f2";
		}
	}

	cateBtn.addEventListener("click", showCategory);
	mainItems.forEach((item) => {
		item.addEventListener("mouseenter", () => {
			const target = item.getAttribute("data-sub");

			subItems.forEach((sub) => {
				if (sub.getAttribute("data-parent") === target) {
					sub.style.display = "block";
				} else {
					sub.style.display = "none";
				}
			});
			// 모든 mainItems 배경 초기화
			mainItems.forEach((i) => (i.style.backgroundColor = ""));
			// 현재 마우스 올린 아이템 배경색 변경
			item.style.backgroundColor = "#f2f2f2"; // 원하는 색으로 변경
		});

		// 마우스 떠나면 배경 초기화
		item.addEventListener("mouseleave", () => {
			item.style.backgroundColor = "";
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

	/*기념일 클릭시 모달 */
	const anvBtn = document.getElementById("anniversary_btn");
	const anvModal = document.getElementById("anniversary_modal");
	const img = document.querySelector("#anniversary_btn img");
	function toggleAnniversary() {
		

		if (img.src.includes("down.png")) {
			// 열기
			anvModal.style.display = "block";
			img.src = "/img/page_up.png";
		} else {
			// 닫기
			anvModal.style.display = "none";
			img.src = "/img/down.png";
		}
	}

	anvBtn.addEventListener("click", toggleAnniversary);

	// 외부 클릭시 모달 닫히게
	document.addEventListener("click", (e) => {
		const isClickCateInside =
			cateModal.contains(e.target) || cateBtn.contains(e.target);
		const isClickSearchInside =
			searchModal.contains(e.target) || openSearchBtn.contains(e.target);
		const isClickAnniInside =
			anvModal.contains(e.target) || anvBtn.contains(e.target);
		if (!isClickCateInside) {
			cateModal.style.display = "none";
		}
		if (!isClickSearchInside) {
			searchModal.style.display = "none";
		}
		if (!isClickAnniInside) {
			anvModal.style.display = "none";
		}
	});
});





