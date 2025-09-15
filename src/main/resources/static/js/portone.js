async function startIdentityVerification() {
	// 랜덤 UUID 생성 (브라우저에서 가능한 crypto.randomUUID() 등)
	const identityVerificationId = `identity-verification-${crypto.randomUUID()}`;

	try {
		const response = await PortOne.requestIdentityVerification({
			storeId: "store-9d7590c1-6b40-4154-b9ce-11ef29752515",
			identityVerificationId: identityVerificationId,
			channelKey: "channel-key-7d80f5ce-b954-4a78-9b15-1a5ce38133fd",
			// redirectUrl 사용하려면 추가 가능:
			redirectUrl: `http://localhost:8888/identity-verification-redirect`,
			//redirectUrl: `${BASE_URL}/identity-verification-redirect`,
			// redirectUrl: `${window.location.origin}/identity-verification-redirect`
		});

		if (response.code !== undefined) {
			// 오류 발생 시 메시지 보여주기
			alert(`본인인증 오류: ${response.message}`);
			return;
		}

		// 성공하면서버로 identityVerificationId 전달
		const serverResponse = await fetch("/identity-verifications", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				// CSRF 필요하면 헤더에 추가
			},
			body: JSON.stringify({
				identityVerificationId: identityVerificationId
			})
		});

		const serverResult = await serverResponse.json();
		if (serverResult.status === "VERIFIED") {
			// 인증 성공 시 이후 처리
			console.log("본인인증 정보:", serverResult.verifiedCustomer);
			// 예: 회원가입 페이지 열기, 이름/생년월일 입력 필드 자동 채움 등
			
		} else {
			alert("본인인증이 완료되지 않았습니다.");
		}

	} catch (err) {
		console.error("본인인증 진행 중 오류:", err);
	}
}

// 버튼 바인딩
document.addEventListener("DOMContentLoaded", () => {
	const btn = document.getElementById("portone");
	if (btn) {
		btn.addEventListener("click", startIdentityVerification);
	}
});