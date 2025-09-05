// 전역 변수
let currentEditId = null;

// DOM 요소들
const editModal = document.getElementById('editModal');
const editModalTitle = document.getElementById('m_title');
const editModalDate = document.getElementById('m_date');
const editModalRepeat = document.getElementById('m_repeat');
const editModalChips = document.querySelectorAll('.chip');
const todayBtn = document.getElementById('todayBtn');
const closeBtn = document.querySelector('.close_btn');
const modalCancel = document.querySelector('.modal_cancel');
const modalSave = document.querySelector('.modal_save');

// 기념일 등록 모달 요소들
let addModal = null;

// 초기화
document.addEventListener('DOMContentLoaded', function() {
  initializeEventListeners();
  setupAddModal();
});

// 이벤트 리스너 초기화
function initializeEventListeners() {
  // 수정 버튼들
  document.querySelectorAll('.edit_btn').forEach(btn => {
    btn.addEventListener('click', handleEditClick);
  });

  // 삭제 버튼들
  document.querySelectorAll('.btn.danger').forEach(btn => {
    btn.addEventListener('click', handleDeleteClick);
  });

  // 기념일 등록하기 버튼
  document.querySelector('.btn.primary.xl').addEventListener('click', openAddModal);

  // 수정 모달 관련
  closeBtn.addEventListener('click', closeEditModal);
  modalCancel.addEventListener('click', closeEditModal);
  modalSave.addEventListener('click', handleSaveEdit);
  todayBtn.addEventListener('click', setTodayDate);

  // 유형 칩 선택
  editModalChips.forEach(chip => {
    chip.addEventListener('click', function() {
      selectChip(this);
    });
  });

  // 모달 배경 클릭으로 닫기
  editModal.addEventListener('click', function(e) {
    if (e.target === editModal) {
      closeEditModal();
    }
  });
}

// 수정 버튼 클릭 처리
function handleEditClick(e) {
  const card = e.target.closest('.anniv_card');
  const id = card.dataset.id;
  const title = card.dataset.title;
  const type = card.dataset.type;
  const date = card.dataset.date;
  const repeat = card.dataset.repeat;

  // 모달에 데이터 채우기
  editModalTitle.value = title;
  editModalDate.value = date;
  editModalRepeat.value = repeat;

  // 유형 칩 선택
  editModalChips.forEach(chip => {
    chip.classList.remove('is_selected');
    if (chip.dataset.type === type) {
      chip.classList.add('is_selected');
    }
  });

  currentEditId = id;
  openEditModal();
}

// 삭제 버튼 클릭 처리
function handleDeleteClick(e) {
  if (confirm('정말로 이 기념일을 삭제하시겠습니까?')) {
    const card = e.target.closest('.anniv_card');
    card.remove();
  }
}

// 수정 모달 열기
function openEditModal() {
  editModal.removeAttribute('hidden');
  document.body.style.overflow = 'hidden';
}

// 수정 모달 닫기
function closeEditModal() {
  editModal.setAttribute('hidden', '');
  document.body.style.overflow = '';
  currentEditId = null;
}

// 수정 저장 처리
function handleSaveEdit() {
  const title = editModalTitle.value.trim();
  const date = editModalDate.value;
  const repeat = editModalRepeat.value;
  const selectedChip = document.querySelector('.chip.is_selected');
  const type = selectedChip ? selectedChip.dataset.type : 'birthday';

  if (!title || !date) {
    alert('제목과 날짜를 모두 입력해주세요.');
    return;
  }

  // 카드 데이터 업데이트
  const card = document.querySelector(`[data-id="${currentEditId}"]`);
  if (card) {
    card.dataset.title = title;
    card.dataset.type = type;
    card.dataset.date = date;
    card.dataset.repeat = repeat;

    // 카드 내용 업데이트
    updateCardContent(card, title, type, date);
  }

  closeEditModal();
}

// 카드 내용 업데이트
function updateCardContent(card, title, type, date) {
  const typeLabel = card.querySelector('.type_label');
  const cardTitle = card.querySelector('.card_title');
  const eventWord = card.querySelector('.event_word');

  // 유형 라벨 업데이트
  const typeText = getTypeText(type);
  typeLabel.textContent = typeText;
  typeLabel.dataset.text = typeText;

  // 제목 업데이트
  const namePart = title.replace(/생일|결혼|기념일|기타/g, '').trim();
  const eventPart = title.replace(namePart, '').trim();
  
  cardTitle.innerHTML = `
    <span class="name_mask">${namePart || 'ㅇㅇㅇ'}</span>
    <strong class="event_word">${eventPart || typeText}</strong>
  `;

  // D-Day 계산 및 업데이트
  updateDDay(card, date);
}

// 유형 텍스트 변환
function getTypeText(type) {
  const typeMap = {
    'birthday': '생일',
    'wedding': '결혼기념일',
    'etc': '연애/기타'
  };
  return typeMap[type] || '생일';
}

// D-Day 계산 및 업데이트
function updateDDay(card, date) {
  const targetDate = new Date(date);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  targetDate.setHours(0, 0, 0, 0);

  const diffTime = targetDate - today;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  const ddayText = card.querySelector('.dday_text');
  const whenText = card.querySelector('.when_text');

  if (diffDays === 0) {
    ddayText.textContent = 'D-DAY';
    whenText.textContent = '오늘';
  } else if (diffDays > 0) {
    ddayText.textContent = `D-${diffDays}`;
    whenText.textContent = `${diffDays}일 후`;
  } else {
    ddayText.textContent = `D+${Math.abs(diffDays)}`;
    whenText.textContent = `${Math.abs(diffDays)}일 전`;
  }
}

// 유형 칩 선택
function selectChip(clickedChip) {
  editModalChips.forEach(chip => {
    chip.classList.remove('is_selected');
  });
  clickedChip.classList.add('is_selected');
}

// 오늘 날짜로 설정
function setTodayDate() {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  editModalDate.value = `${year}-${month}-${day}`;
}

// 기념일 등록 모달 설정
function setupAddModal() {
  addModal = document.getElementById('addModal');
  setupAddModalEvents();
}

// 등록 모달 이벤트 설정
function setupAddModalEvents() {
  const addModalCloseBtn = addModal.querySelector('.close_btn');
  const addModalCancel = addModal.querySelector('.add_modal_cancel');
  const addModalSave = addModal.querySelector('.add_modal_save');
  const addTodayBtn = addModal.querySelector('#addTodayBtn');
  const addModalChips = addModal.querySelectorAll('.chip');

  addModalCloseBtn.addEventListener('click', closeAddModal);
  addModalCancel.addEventListener('click', closeAddModal);
  addModalSave.addEventListener('click', handleSaveAdd);
  addTodayBtn.addEventListener('click', setAddTodayDate);

  addModalChips.forEach(chip => {
    chip.addEventListener('click', function() {
      selectAddChip(this);
    });
  });

  addModal.addEventListener('click', function(e) {
    if (e.target === addModal) {
      closeAddModal();
    }
  });
}

// 등록 모달 열기
function openAddModal() {
  addModal.removeAttribute('hidden');
  document.body.style.overflow = 'hidden';
}

// 등록 모달 닫기
function closeAddModal() {
  addModal.setAttribute('hidden', '');
  document.body.style.overflow = '';
  
  // 폼 초기화
  addModal.querySelector('#add_title').value = '';
  addModal.querySelector('#add_date').value = '';
  addModal.querySelector('#add_repeat').value = 'yearly';
  
  // 유형 칩 초기화
  addModal.querySelectorAll('.chip').forEach(chip => {
    chip.classList.remove('is_selected');
  });
  addModal.querySelector('.chip[data-type="birthday"]').classList.add('is_selected');
}

// 등록 저장 처리
function handleSaveAdd() {
  const title = addModal.querySelector('#add_title').value.trim();
  const date = addModal.querySelector('#add_date').value;
  const repeat = addModal.querySelector('#add_repeat').value;
  const selectedChip = addModal.querySelector('.chip.is_selected');
  const type = selectedChip ? selectedChip.dataset.type : 'birthday';

  if (!title || !date) {
    alert('제목과 날짜를 모두 입력해주세요.');
    return;
  }

  // 새 카드 생성
  createNewCard(title, type, date, repeat);
  closeAddModal();
}

// 새 카드 생성
function createNewCard(title, type, date, repeat) {
  const newId = 'a' + Date.now(); // 간단한 ID 생성
  const typeText = getTypeText(type);
  
  // 날짜에서 월 추출
  const dateObj = new Date(date);
  const year = dateObj.getFullYear();
  const month = dateObj.getMonth() + 1;
  const monthKey = `month_${year}_${String(month).padStart(2, '0')}`;
  
  // 해당 월 섹션 찾기 또는 생성
  let monthSection = document.getElementById(monthKey);
  if (!monthSection) {
    monthSection = createMonthSection(year, month, monthKey);
  }

  // 새 카드 HTML 생성
  const newCard = document.createElement('article');
  newCard.className = 'anniv_card';
  newCard.setAttribute('data-id', newId);
  newCard.setAttribute('data-title', title);
  newCard.setAttribute('data-type', type);
  newCard.setAttribute('data-date', date);
  newCard.setAttribute('data-repeat', repeat);
  newCard.setAttribute('aria-label', '기념일 카드');

  const namePart = title.replace(/생일|결혼|기념일|기타/g, '').trim();
  const eventPart = title.replace(namePart, '').trim();

  newCard.innerHTML = `
    <div class="left_stick" aria-hidden="true"></div>

    <section class="card_body" aria-describedby="${newId}_desc">
      <p id="${newId}_desc" class="card_meta">
        <span class="type_label" data-text="${typeText}">${typeText}</span>
        <span class="dot" aria-hidden="true">·</span>
        <span class="when_text">${getWhenText(date)}</span>
      </p>

      <p class="card_title">
        <span class="name_mask">${namePart || 'ㅇㅇㅇ'}</span>
        <strong class="event_word">${eventPart || typeText}</strong>
      </p>
    </section>

    <aside class="card_side">
      <p class="dday_box" aria-label="디데이 표시">
        <span class="dday_text">${getDDayText(date)}</span>
      </p>

      <div class="card_actions">
        <button class="btn ghost" type="button">선물 추천받기</button>
        <div class="btn_row">
          <button class="btn line edit_btn" type="button">수정</button>
          <button class="btn line danger" type="button">삭제</button>
        </div>
      </div>
    </aside>
  `;

  // 이벤트 리스너 추가
  newCard.querySelector('.edit_btn').addEventListener('click', handleEditClick);
  newCard.querySelector('.btn.danger').addEventListener('click', handleDeleteClick);

  // 월 섹션에 카드 추가
  monthSection.appendChild(newCard);
}

// 월 섹션 생성
function createMonthSection(year, month, monthKey) {
  const monthSection = document.createElement('section');
  monthSection.className = 'month_section';
  monthSection.setAttribute('aria-labelledby', monthKey);
  
  monthSection.innerHTML = `
    <h3 id="${monthKey}" class="month_title">${year}년 ${String(month).padStart(2, '0')}월</h3>
  `;

  // 기존 섹션들 사이에 적절한 위치에 삽입
  const existingSections = document.querySelectorAll('.month_section');
  let insertPosition = null;

  for (let section of existingSections) {
    const sectionId = section.id;
    const sectionYear = parseInt(sectionId.split('_')[1]);
    const sectionMonth = parseInt(sectionId.split('_')[2]);

    if (year < sectionYear || (year === sectionYear && month < sectionMonth)) {
      insertPosition = section;
      break;
    }
  }

  if (insertPosition) {
    insertPosition.parentNode.insertBefore(monthSection, insertPosition);
  } else {
    document.querySelector('.anniversary_panel').appendChild(monthSection);
  }

  return monthSection;
}

// 유형 칩 선택 (등록 모달용)
function selectAddChip(clickedChip) {
  addModal.querySelectorAll('.chip').forEach(chip => {
    chip.classList.remove('is_selected');
  });
  clickedChip.classList.add('is_selected');
}

// 오늘 날짜로 설정 (등록 모달용)
function setAddTodayDate() {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  addModal.querySelector('#add_date').value = `${year}-${month}-${day}`;
}

// D-Day 텍스트 계산
function getDDayText(date) {
  const targetDate = new Date(date);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  targetDate.setHours(0, 0, 0, 0);

  const diffTime = targetDate - today;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  if (diffDays === 0) {
    return 'D-DAY';
  } else if (diffDays > 0) {
    return `D-${diffDays}`;
  } else {
    return `D+${Math.abs(diffDays)}`;
  }
}

// When 텍스트 계산
function getWhenText(date) {
  const targetDate = new Date(date);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  targetDate.setHours(0, 0, 0, 0);

  const diffTime = targetDate - today;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  if (diffDays === 0) {
    return '오늘';
  } else if (diffDays > 0) {
    return `${diffDays}일 후`;
  } else {
    return `${Math.abs(diffDays)}일 전`;
  }
}