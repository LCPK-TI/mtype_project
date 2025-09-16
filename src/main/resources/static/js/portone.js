document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("portone").addEventListener("click", function() {
		PortOne.requestIdentityVerification({
			storeId: "store-9d7590c1-6b40-4154-b9ce-11ef29752515",
			channelKey: "channel-key-7d80f5ce-b954-4a78-9b15-1a5ce38133fd",
			identityVerificationId: `identity-verification-${crypto.randomUUID()}`
		}).then(rsp => {
			console.log("identityVerificationId:", rsp.identityVerificationId);
			console.log("identityVerificationTxId:", rsp.identityVerificationTxId);
			console.log("transactionType:", rsp.transactionType);

			// 서버로 ID 전송
			fetch("/verify/complete", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ identityVerificationId: rsp.identityVerificationId })
			})
				.then(res => res.json())
				.then(data => {
					alert(
						"인증 성공!\n이름: " + data.name +
						"\n생년월일: " + data.birthday +
						"\n번호: " + data.phone
					);
				});
		})
			.catch(err => {
				console.error("PortOne SDK error:", err);
				alert("SDK 호출 오류: " + err);
			});
	});
});
