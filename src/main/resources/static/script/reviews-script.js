
let message= document.querySelector('.message')




async function reviews(param,page) {
    let url=''
    let url2=''
    message=document.querySelector('.message')
    {message.style.display='none'}
    let token;
    for (let i = 0; i < sessionStorage.length; i++) {
        const key = sessionStorage.key(i);
        if (key.startsWith('tokenUser')) {
            token = sessionStorage.getItem(key);
            break;
        }
    }
    if(param=='all'){
        url=`/api/user/allReview?page=${page}`
        url2='/api/user/countAllReview'
    } else if(param=='my'){
        url=`/api/user/myReview?page=${page}`
        url2='/api/user/countMyReview'
    }

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };

    try {
        const [listRes, countRes] = await Promise.all([
            fetch(url, { headers }),
            fetch(url2, { headers })
        ]);

        if (listRes.status==204) {
            document.querySelector('.reviews-container').replaceChildren()
            console.log()
            message.style.display='block'
            message.innerText='Лист отзывов пуст!'
            return

        }

        const list = await listRes.json();
        const totalCount = await countRes.json();

        setReviews(list)
        pagination(param,totalCount)

    } catch (e) {
        console.error(e);

    }
}


function setReviews(data){
    let conteiner=document.querySelector('.reviews-container')
    conteiner.replaceChildren()
    for (let index = 0; index < data.length; index++) {
        let str=`<div class="review">
            <div class="review-header">
                <strong>${data[index].userName}</strong>
                <span>${data[index].date}</span>
            </div>
            <div class="review-text">
              ${data[index].text}
            </div>
        </div>`
        conteiner.insertAdjacentHTML("beforeend",str)

    }}

let currentPage = 0;
let currentStart = 1; // первая страница в диапазоне кнопок
const pageSize = 10;

function pagination(param, totalCount) {

    const paginationContainer = document.querySelector('.pagination');
    if (totalCount === 0) {
        paginationContainer.style.display = 'none';
        currentPage = 0;   // сброс
        currentStart = 1;  // сброс
        return;
    }else {
        paginationContainer.style.display = 'block';
    }
    paginationContainer.innerHTML = '';
    paginationContainer.style.display = 'flex';
    paginationContainer.style.justifyContent = 'center';
    paginationContainer.style.alignItems = 'center';
    paginationContainer.style.gap = '5px';
    paginationContainer.style.margin = '20px 0';

    const totalPages = Math.ceil(totalCount / pageSize);

    // Создаем стрелки
    const prevBtn = document.createElement('button');
    prevBtn.innerText = '‹';
    prevBtn.style.padding = '5px 10px';
    const nextBtn = document.createElement('button');
    nextBtn.innerText = '›';
    nextBtn.style.padding = '5px 10px';

    paginationContainer.appendChild(prevBtn);

    const pagesDiv = document.createElement('div');
    pagesDiv.style.display = 'flex';
    pagesDiv.style.gap = '5px';
    paginationContainer.appendChild(pagesDiv);

    paginationContainer.appendChild(nextBtn);

    function renderPages() {
        pagesDiv.innerHTML = '';
        const endPage = Math.min(currentStart + 9, totalPages);

        for (let i = currentStart; i <= endPage; i++) {
            const btn = document.createElement('button');
            btn.innerText = i;
            btn.style.padding = '5px 10px';
            btn.style.border = '1px solid #ccc';
            btn.style.borderRadius = '3px';
            btn.style.cursor = 'pointer';
            btn.style.backgroundColor = i === currentPage + 1 ? 'green' : 'white';
            btn.style.color = i === currentPage + 1 ? 'white' : 'black';

            btn.addEventListener('click', () => {
                currentPage = i - 1;
                // Автообновление диапазона кнопок
                if (i < currentStart) currentStart = i;
                if (i >= currentStart + 10) currentStart = i - 9;

                reviews(param, currentPage);
                renderPages();
            });

            pagesDiv.appendChild(btn);
        }

        prevBtn.style.display = currentStart > 1 ? 'inline-block' : 'none';
        nextBtn.style.display = endPage < totalPages ? 'inline-block' : 'none';
    }

    prevBtn.onclick = () => {
        currentStart = Math.max(currentStart - 10, 1);
        renderPages();
    };

    nextBtn.onclick = () => {
        currentStart = Math.min(currentStart + 10, Math.max(totalPages - 9, 1));
        renderPages();
    };

    renderPages();
}


async function saveRev() {
    try {

        const textrev = document.querySelector('.textrev').value.trim();

        if (!textrev) {
            alert('Текст отзыва пустой!');
            return;
        }

        // Формируем объект данных
        const data = { text: textrev };

        // Получаем токен пользователя из sessionStorage
        let token = sessionStorage.getItem('tokenUser');


        // Отправляем POST-запрос
        const response = await fetch('/api/user/saveReview', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка при сохранении отзыва');
        }

        // Успешно
        alert('Отзыв успешно отправлен на модерацию!');
        document.querySelector('.textrev').value = ''; // очищаем поле


    } catch (err) {
        console.error('Ошибка при сохранении отзыва:', err);
        alert(err.message);
    }
}

