// 전화번호, 이메일 버튼 클릭 시 인증
document.querySelectorAll(".input_toggle").forEach((btn) => {
  btn.addEventListener("click", () => {
    const infoRow = btn.closest(".info");
    const mainInput = infoRow.querySelector(".info_input");

    const isVerifyMode = infoRow.classList.contains("verify_mode");

    if (!isVerifyMode) {
      // 인증 모드
      mainInput.classList.add("shrink");
      mainInput.removeAttribute("readonly");

      const sendBtn = document.createElement("button");
      sendBtn.type = "button";
      sendBtn.textContent =
        mainInput.type === "email" ? "인증메일 발송" : "인증번호 발송";
      sendBtn.className = "btn_info_edit btn_send_code";

      const verifyInput = document.createElement("input");
      verifyInput.type = "text";
      verifyInput.placeholder = "인증번호 입력";
      verifyInput.className = "info_input verify_input";

      const saveBtn = document.createElement("button");
      saveBtn.type = "button";
      saveBtn.textContent =
        mainInput.type === "email" ? "이메일 변경" : "전화번호 변경";
      saveBtn.className = "btn_info_edit btn_save_change";

      // 버튼 뒤에 요소 추가
      btn.insertAdjacentElement("afterend", saveBtn);
      btn.insertAdjacentElement("afterend", verifyInput);
      btn.insertAdjacentElement("afterend", sendBtn);

      infoRow.classList.add("verify_mode");
      btn.textContent = "취소";

      // 저장 버튼 클릭 => 원상복구
      saveBtn.addEventListener("click", () => {
        mainInput.classList.remove("shrink");
        mainInput.setAttribute("readonly", true);
        sendBtn.remove();
        verifyInput.remove();
        saveBtn.remove();
        infoRow.classList.remove("verify_mode");
        btn.textContent =
          mainInput.type === "email" ? "이메일 변경" : "본인인증";
      });
    } else {
      // 취소 시 원상복구
      mainInput.classList.remove("shrink");
      mainInput.setAttribute("readonly", true);
      infoRow
        .querySelectorAll(".btn_send_code, .verify_input, .btn_save_change")
        .forEach((el) => el.remove());
      infoRow.classList.remove("verify_mode");
      btn.textContent = mainInput.type === "email" ? "이메일 변경" : "본인인증";
    }
  });
});

// 주소. 우편번호 검색 클릭 시  인풋 활성화와 주소 변경 버튼 활성화
const postcodeBtn = document.querySelector(".btn_postcode");
const saveAddrBtn = document.querySelector(".btn_chg_addr");
const addrInputs = document.querySelectorAll(".addr_input .info_input");

postcodeBtn.addEventListener("click", () => {
  addrInputs.forEach((input) => input.removeAttribute("readonly"));
  saveAddrBtn.removeAttribute("disabled");
  console.log("우편번호 검색 클릭 → 주소 입력 활성화");
  // TODO: 우편번호 검색 API 연동
});

// 주소 변경 저장
saveAddrBtn.addEventListener("click", () => {
  if (saveAddrBtn.hasAttribute("disabled")) return;

  const postcode = document.querySelector(".postcode").value;
  const addr1 = document.querySelector(".addr1").value;
  const addr2 = document.querySelector(".addr2").value;

  console.log("저장할 주소:", postcode, addr1, addr2);

  addrInputs.forEach((input) => input.setAttribute("readonly", true));
  saveAddrBtn.setAttribute("disabled", true);

  //서버 전송
});
