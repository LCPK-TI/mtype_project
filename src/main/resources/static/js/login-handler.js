document.addEventListener("DOMContentLoaded", function(){
	const urlParams = new URLSearchParams(window.location.search);
	const status = urlParams.get("status");
	const kakaoId = urlParams.get("id");
	const errorMsg = urlParams.get("error");
	
	if(status === "withdrawn" && kakaoId){
		if(confirm("탈퇴된 회원입니다. 탈퇴를 취소하시겠습니까?")){
			fetch("/api/user/reactivate", {
				method: "POST",
				headers: {"Content-Type": "application/json"},
				body: JSON.stringify({kakaoUserId: kakaoId}),
			})
			.then(response =>{
				if(!response.ok) {throw new Error("회원 복구 실패");}
				return response.text();
			})
			.then(newJwtToken => {
				localStorage.setItem("jwt", newJwtToken);
				alert("계정이 복구되었습니다. MTYPE을 이용하실 수 있습니다.");
				window.location.href = "/";
			})
			.catch(error => {
				console.error("Error: ", error);
				alert(error.message);
			});
		}
	}
	if(errorMsg){
		alert(decodeURIComponent(errorMsg));
	}
})