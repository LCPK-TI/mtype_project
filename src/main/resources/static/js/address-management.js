const API = "/api/addresses";

let addresses = [];

function esc(s){
  return String(s ?? "")
    .replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;")
    .replaceAll('"',"&quot;").replaceAll("'","&#39;");
}

// 서버 응답
function normalize(it){
  return {
    id:        it.id        ?? it.addrNo,
    name:      it.name      ?? it.addrName,
    receiver:  it.receiver  ?? it.recipient,
    phone:     it.phone     ?? it.reciPhone ?? "",
    addr1:     it.addr1     ?? it.addrMain,
    addr2:     it.addr2     ?? it.addrDetail ?? "",
    isDefault: (it.isDefault ?? it.addrDefault) ? true : false
  };
}

function rowTemplate(a){
  return `
    <article class="address_card" data-id="${a.id}" aria-label="${esc(a.name)} 배송지">
      <div class="address_body">
        <dl class="info_list">
          <div class="info_item">
            <dt class="info_label">배송지명</dt>
            <dd class="info_value">
              <span class="addr_name">${esc(a.name)}</span>
              ${a.isDefault ? '<span class="badge_default" aria-label="기본 배송지">기본배송지</span>' : ''}
            </dd>
          </div>
          <div class="info_item">
            <dt class="info_label">수령인</dt>
            <dd class="info_value">${esc(a.receiver)}</dd>
          </div>
          <div class="info_item">
            <dt class="info_label">연락처</dt>
            <dd class="info_value">${a.phone ? esc(a.phone) : '-'}</dd>
          </div>
          <div class="info_item">
            <dt class="info_label">주소</dt>
            <dd class="info_value">${esc(a.addr1)} ${esc(a.addr2)}</dd>
          </div>
        </dl>
      </div>
      <div class="address_actions">
        <button type="button" class="btn_small" data-action="edit" title="수정">수정</button>
        <button type="button" class="btn_small" data-action="remove" title="삭제">삭제</button>
      </div>
    </article>
  `;
}

function render(){
  const list = document.querySelector(".address_list");
  list.innerHTML = addresses.length
    ? addresses.map(rowTemplate).join("") + '<hr class="row_divider" aria-hidden="true" />'
    : '<p style="color:#777; text-align:center; padding:2rem;">등록된 배송지가 없습니다. 우측 상단의 <b>배송지 추가</b> 버튼으로 등록하세요.</p>';
}

//다이얼로그 
const dialog     = document.getElementById("address_dialog");
const form       = document.getElementById("address_form");
const formTitle  = document.getElementById("dialog_title");

function openDialog(mode, data){
  form.reset();
  formTitle.textContent = (mode === "edit") ? "배송지 수정" : "배송지 추가";

  if(mode === "edit" && data){
    form.id.value           = data.id;
    form.name.value         = data.name ?? "";
    form.receiver.value     = data.receiver ?? "";
    form.phone.value        = data.phone ?? "";
    form.addr1.value        = data.addr1 ?? "";
    form.addr2.value        = data.addr2 ?? "";
    form.isDefault.checked  = !!data.isDefault;
  }else{
    form.id.value = "";
    form.isDefault.checked = false;
  }

  dialog.showModal();
  form.name.focus();
}
function closeDialog(){ dialog.close(); }

document.querySelector(".btn_primary")
  .addEventListener("click", ()=> openDialog("create"));
document.getElementById("btn_close").addEventListener("click", closeDialog);
document.getElementById("btn_cancel").addEventListener("click", closeDialog);

// 서버 연동 
async function load(){
  const res = await fetch(API, { headers: { "Accept":"application/json" }});
  if(!res.ok) { console.error("목록 조회 실패", res.status); return; }
  const data = await res.json();
  addresses = (Array.isArray(data) ? data : []).map(normalize);
  render();
}

async function saveToServer(data){
  const url = data.id ? `${API}/${data.id}` : API;
  const method = data.id ? "PUT" : "POST";
  const payload = {
    name: data.name,
    receiver: data.receiver,
    phone: data.phone,
    addr1: data.addr1,
    addr2: data.addr2,
    isDefault: data.isDefault
  };
  const res = await fetch(url, {
    method,
    headers: { "Content-Type":"application/json", "Accept":"application/json" },
    body: JSON.stringify(payload)
  });
  if(!res.ok){
    const msg = await res.text().catch(()=> "");
    alert(`저장 실패 (${res.status})\n${msg}`);
    return false;
  }
  return true;
}

async function removeFromServer(id){
  const res = await fetch(`${API}/${id}`, { method: "DELETE" });
  if(!res.ok){
    const msg = await res.text().catch(()=> "");
    alert(`삭제 실패 (${res.status})\n${msg}`);
    return false;
  }
  return true;
}

// 목록/수정/삭제 
document.querySelector(".address_list").addEventListener("click", async (e)=>{
  const btn = e.target.closest(".btn_small");
  if(!btn) return;

  const row = e.target.closest(".address_card");
  const id  = Number(row?.dataset.id);
  const item = addresses.find(a => a.id === id);
  if(!item) return;

  if(btn.dataset.action === "edit"){
    openDialog("edit", item);
  }else if(btn.dataset.action === "remove"){
    if(confirm("배송지를 삭제할까요?")){
      const ok = await removeFromServer(id);
      if(ok) await load();
    }
  }
});

// 저장
form.addEventListener("submit", async (e)=>{
  e.preventDefault();

  const data = {
    id: form.id.value ? Number(form.id.value) : null,
    name: form.name.value.trim(),
    receiver: form.receiver.value.trim(),
    phone: form.phone.value.trim(),
    addr1: form.addr1.value.trim(),
    addr2: form.addr2.value.trim(),
    isDefault: form.isDefault.checked
  };

  if(!data.name || !data.receiver || !data.addr1){
    alert("배송지명, 수령인, 주소는 필수입니다.");
    return;
  }

  const ok = await saveToServer(data);
  if(!ok) return;

  closeDialog();
  await load(); // 서버 데이터로 다시 그리기
});

// 우편번호
const btnSearch = document.getElementById("btn_search_address");
if (btnSearch) btnSearch.addEventListener("click", openPostcode);

function openPostcode(){
  if(!(window.daum && window.daum.Postcode)){
    alert("우편번호 스크립트를 로드하세요.");
    return;
  }
  new daum.Postcode({
    oncomplete: function(data){
      const addr = (data.userSelectedType === 'R') ? data.roadAddress : data.jibunAddress;
      form.addr1.value = addr || "";
      form.addr2.focus();
    }
  }).open({ q: form.addr1?.value || "" });
}

// 최초 로드 
document.addEventListener("DOMContentLoaded", load);