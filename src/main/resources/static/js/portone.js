document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("portone").addEventListener("click", function() {
        PortOne.requestIdentityVerification({
            storeId: portoneStoreId,
            channelKey: portoneChannelKey,
            identityVerificationId: `identity-verification-${crypto.randomUUID()}`
        
        }).then(response => {
            // 인증 성공 시 백엔드의 API를 호출.
            fetch("/api/user/verify", { 
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ identityVerificationId: response.identityVerificationId })
            })
            .then(res => {
                if (!res.ok) {
                    throw new Error("서버 응답 오류: " + res.status);
                }
                return res.text();
            })
            .then(message => {
                alert(message); // 백엔드에서 보낸 성공 메시지를 표시
                // window.location.href = "/user/mypage"; 
            });

        }).catch(error => {
            // 인증 실패 또는 오류 발생
            alert("인증에 실패했습니다: " + error.message);
        });
    });
});