window.addEventListener("DOMContentLoaded",()=>{
	//챗봇 열기,닫기
	const chatbotBtn = document.getElementById("cb_btn");
	function showChatbot(){
		chatbotBtn.style.display = "none";
		document.getElementById("chatbot_modal").style.display = "block";
		
	}
	const closeBtn = document.querySelector("#chatbot_header button");
	function closeChatbot(){
		document.getElementById("chatbot_modal").style.display = "none";
		chatbotBtn.style.display = "flex";
	}
	closeBtn.addEventListener("click",closeChatbot);
	
	chatbotBtn.addEventListener("click",showChatbot);
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

	const pageupBtn = document.getElementById("page_up");
	pageupBtn.addEventListener("click",()=>{
		window.scrollTo({
			top:0,
			behavior:"smooth"
		})
	});

})
