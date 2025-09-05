//챗봇 열기

//페이지 맨 위로
//스크롤 위치가 100px 이상일 때 위로 가기 버튼을 보이게 함
window.addEventListener("scroll",()=>{
	if(document.body.scrollTop>150||
		document.documentElement.scrollTop > 20
	){
		document.getElementById("page_up").style.display ="block";
	}else{
		document.getElementById("page_up").style.display ="none";
	}
})
