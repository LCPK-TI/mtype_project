document.addEventListener("DOMContentLoaded", function(){
	const kakaoLoginBtn = document.getElementById("kakao-login-btn");
	
	if(kakaoLoginBtn){
		kakaoLoginBtn.addEventListener("click", function(){
			const KAKAO_REST_API_KEY = "a8494562c83664af411ac675d25a6062";
			const KAKAO_REDIRECT_URI = "http://localhost:8888/oauth/kakao/redirect";
			
			const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;
			
			window.location.href = kakaoAuthUrl;
		});
	}
})