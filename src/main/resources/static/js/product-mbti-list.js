document.addEventListener("DOMContentLoaded", function() {
	const mbtiItems = document.querySelectorAll('.mbti');

	mbtiItems.forEach(li => {
		li.addEventListener("click", () => {
			const categoryList = li.querySelector(".category_list");
			const isActive = categoryList.classList.contains("active");
			// 모든 리스트 닫기
			document.querySelectorAll(".category_list").forEach(list => {
				list.classList.remove("active");

			});
			document.querySelectorAll(".mbti").forEach(item => {
				item.classList.remove("active");
			});
			// 원래 열려있던 거면 닫고, 아니면 열기
			if (!isActive) {
			categoryList.classList.add("active");
			li.classList.add("active");  // li에도 active 추가
			li.parentElement.querySelector("img").src="/img/page_up.png";
		}
	});
});
});