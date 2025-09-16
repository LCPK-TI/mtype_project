document.addEventListener('DOMContentLoaded', function() {
	// 마이페이지 최근 본 상품 더보기
	const moreRecentViewsBtn = document.getElementById("more-recent-views-btn");
	if (moreRecentViewsBtn) {
		moreRecentViewsBtn.addEventListener('click', function() {
			if(window.isUserLoggedIn){
				window.location.href = "/user/recent-views";
			}else{
				alert("로그인 후 이용해주세요!");
				window.location.href = "/user/login";
			}
		});
	}
});
