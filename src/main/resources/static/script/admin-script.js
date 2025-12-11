let userForBlock=[]
let userForUnBlock=[]
let goodReviews=[]
let badReviews=[]
let empty=document.querySelector('.empty')

async function exit(){
    localStorage.removeItem('tokenAdmin')
    try {
        let response = await fetch('/api/auth/vhod')
        if (!response.ok) {
            throw new Error('Ошибка при выходе с сервера');
        }

        window.location.href = '/api/auth/vhod';
    }
    catch (error) {
        console.error('Ошибка выхода:', error);
        // Можно показать уведомление пользователю
        alert('Не удалось выйти. Попробуйте ещё раз.');
    }
}
function toggleSearch(){
    document.querySelector('.search-container').style.display="block"
    document.querySelector('.search-container').children[0].value=''
    document.querySelector('.search-container').children[1].selectedIndex=0
    document.querySelector('#table-container').replaceChildren()
    document.querySelector('#pagination-container').style.display="none"
    empty.style.display="none"
    currentPage = 1;
    currentStart = 1;
}
async function setGroup(params, page, clear) {
    empty.style.display="none"
    let url = '';
    let url2 = '';
    if (params === 'all') {
        url = `/api/admin/all?page=${page}`;
        url2 = '/api/admin/countAll';
    } else if (params === 'blocked') {
        url = `/api/admin/allBlocked?page=${page}`;
        url2 = '/api/admin/countAllBlocked';
    } else if (params === 'active') {
        url = `/api/admin/allUnBlocked?page=${page}`;
        url2 = '/api/admin/countAllUnBlocked';
    } else if (params === 'temp') {
        url = `/api/admin/allLocked?page=${page}`;
        url2 = '/api/admin/countAllLocked';
    }else if (params === 'find') {
        let typeSearch=document.querySelector('select').value
        let val=document.querySelector('#search-input').value
        if(typeSearch.toLowerCase()=='id'){
            if (!/^\d+$/.test(val)) {
                alert('Нужно ввести целое число!');
                return;
            }
        }
        url = `/api/admin/find?page=${page}&param=${typeSearch}&value=${val}`;
        url2 = `/api/admin/countFind?param=${typeSearch}&value=${val}`;
    }else if(params==='reviews'){
        url = `/api/admin/filterReviews?page=${page}`;
        url2 = '/api/admin/countReviewsByFilter';
    }

    const headers = {
        'Authorization': 'Bearer ' + localStorage.getItem('tokenAdmin'),
        'Content-Type': 'application/json'
    };

    try {

        const [responseUsers, responseCount] = await Promise.all([
            fetch(url, { headers }),
            fetch(url2, { headers })
        ]);

        if(responseUsers.status==204){
            clearHelp()
            document.querySelector('#table-container').replaceChildren()
            document.querySelector('#pagination-container').style.display='none'
            document.querySelector('.search-container').style.display="none"
            empty.style.display="block"
            return
        }

        if (!responseUsers.ok) {
            throw new Error('Ошибка пользователей: ' + responseUsers.status);
        }
        if (!responseCount.ok) {
            throw new Error('Ошибка подсчёта: ' + responseCount.status);
        }

        const data = await responseUsers.json();
        const countUserInGroup = await responseCount.json();
        if(page==0){
            currentPage = 1;
            currentStart = 1;
        }
        if(clear=='clear'){clearHelp();}

        setTable(data, params);
        createPagination(params, countUserInGroup);

    } catch (error) {
        console.error(error);
        return [];
    }
}

function clearHelp(){
    userForBlock.length=0
    userForUnBlock.length=0
    goodReviews.length=0
    badReviews.length=0
    document.querySelector('.search-container').style.display="none"

}

function setTable(data,typeTable){
    let table= document.querySelector('#table-container')
    table.replaceChildren()
    let dopHeader=''
    let dopBody=''
    let header=''
    let body=''
    if(typeTable=='reviews'){
        header='  <th>reviewId</th>\n                <th>userId</th>\n                <th>text</th>\n     <th>Function</th>'

    }else {
        header=` <th>id</th>
        <th>Name</th>
        <th>Email</th>
        <th>isBlocked</th>
        <th>isLocked</th> `

         }
    if(typeTable=='blocked' || typeTable=='temp'){
        dopHeader='  <th>Reason for blocking</th> <th>Function</th>'
    }
    if(typeTable=='active' || typeTable=='find'){
        dopHeader='  <th>Reason for blocking</th> <th>Function</th>'
        dopBody=`  <td><input type="text" placeholder="Причина"></td> <td><button onclick="arrayUserUnBlock(event.target.parentElement.parentElement.dataset.email)">Заблок.</button></td>`
    }
    let str=`    <table border="1">
        <thead>
            <tr>
                ${header}
              ${dopHeader}
            </tr>
        </thead>
        <tbody>
            </tbody>
    </table>`
    table.insertAdjacentHTML("beforeend",str)
    let tbody=document.querySelector('tbody')
    for (let index = 0; index < data.length; index++) {
 if(typeTable==='reviews'){
     body=`<td>${data[index].reviewId}</td>\n    <td>${data[index].userId}</td>\n    <td>${data[index].text}</td> <td><button onclick="arrayGoodReviews('${data[index].reviewId}')">Одобрить</button>
<button onclick="arrayBadReviews('${data[index].reviewId}')">Отклонить</button></td>`
 } else {
     body=`<td>${data[index].idDTO}</td>\n    <td>${data[index].usernameDTO}</td>\n    <td>${data[index].emailDTO}</td>\n    <td>${data[index].blockedDTO}</td>\n    <td>${data[index].lockedDTO}</td>`

 }
        if(typeTable=='find'){
            if(usersList[index].blockedDTO){
                dopBody=`  <td>${data[index].whyBlockedDTO}</td> <td><button onclick="arrayUserBlock('${data[index].emailDTO}')">Разблок.</button></td>`

            } else{
                dopBody=`  <td><input type="text" placeholder="Причина"></td> <td><button onclick="arrayUserUnBlock('${data[index].emailDTO}')">Заблок.</button></td>`
            }
        }

        if(typeTable=='blocked' || typeTable=='temp'){
            dopBody=`  <td>${data[index].whyBlockedDTO}</td> <td><button onclick="arrayUserBlock(event.target.parentElement.parentElement.dataset.email)">Разблок.</button></td>`

        }

        tbody.insertAdjacentHTML("beforeend",`<tr data-email="${data[index].emailDTO}" data-id="${data[index].reviewId}">
    ${body}
    ${dopBody}
</tr>`)
        if(typeTable=='blocked'||typeTable=='temp' || typeTable=='find'){
            if(userForBlock.includes(data[index].emailDTO)){
                document.querySelector(`tr[data-email="${data[index].emailDTO}"]`).children[6].innerText='✅'
            }
        }
        if(typeTable=='active' || typeTable=='find'){
            if (userForUnBlock.some(u => u.email ==data[index].emailDTO)) {
                document.querySelector(`tr[data-email="${data[index].emailDTO}"]`).children[6].innerText='✅'
            }
        }

        if(typeTable=='reviews'){
            if(goodReviews.includes(data[index].reviewId)){
                document.querySelector(`tr[data-id="${data[index].reviewId}"]`).children[3].children[0].innerText='✅'
                document.querySelector(`tr[data-id="${data[index].reviewId}"]`).children[3].children[1].innerText=''
            }
            if(badReviews.includes(data[index].reviewId)){
                document.querySelector(`tr[data-id="${data[index].reviewId}"]`).children[3].children[0].innerText=''
                document.querySelector(`tr[data-id="${data[index].reviewId}"]`).children[3].children[1].innerText='✅'
            }
        }
    }

    for (let row of tbody.querySelectorAll('tr')) {
        row.dataset.idDTO = row.children[0].innerText;
        row.dataset.usernameDTO = row.children[1].innerText;
        row.dataset.emailDTO = row.children[2].innerText;
    }

    if(typeTable !='reviews') {
        makeSortable()
    }

    let dopButton=''
    if(typeTable=='all'){
        dopButton='<div>\n      <button onclick="reverse()">Назад</button>\n</div>'
    } else dopButton=`<div>\n    <button onclick="save('${typeTable}')">Сохранить</button>\n    <button onclick="reverse()">Назад</button>\n</div>`
    table.insertAdjacentHTML("beforeend",dopButton)


}
let currentPage = 1;
let currentStart = 1;

function createPagination(typeTable, totalUsers) {
    document.querySelector('#pagination-container').style.display="block";
    const pagesDiv = document.querySelector('.pages');
    const prevBtn = document.querySelector('#pagination-container > button:first-child');
    const nextBtn = document.querySelector('#pagination-container > button:last-child');

    const totalPages = Math.ceil(totalUsers / 10);

    function renderPages() {
        pagesDiv.innerHTML = '';
        const endPage = Math.min(currentStart + 9, totalPages);

        for (let i = currentStart; i <= endPage; i++) {
            const btn = document.createElement('button');
            btn.innerText = i;

            btn.classList.toggle('active', i === currentPage);

            btn.addEventListener('click', () => {
                currentPage = i;
                setGroup(typeTable, i - 1);
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



function arrayUserBlock(email){
    userForBlock.push(email)
    document.querySelector(`tr[data-email="${email}"]`).children[6].innerText='✅'
}

function arrayUserUnBlock(email){
    let whyBlock=document.querySelector(`tr[data-email="${email}"]`).children[5].children[0].value

    const user={
        email: email,
        resone: whyBlock

    }
    userForUnBlock.push(user)
    document.querySelector(`tr[data-email="${email}"]`).children[6].innerText='✅'
}

function arrayGoodReviews(id){
    goodReviews.push(Number(id))
    document.querySelector(`tr[data-id="${id}"]`).children[3].children[0].innerText='✅'
    document.querySelector(`tr[data-id="${id}"]`).children[3].children[1].innerText=''
}

function arrayBadReviews(id){
    badReviews.push(Number(id))
    document.querySelector(`tr[data-id="${id}"]`).children[3].children[0].innerText=''
    document.querySelector(`tr[data-id="${id}"]`).children[3].children[1].innerText='✅'
}

function reverse()
{
    userForBlock.length=0
    userForUnBlock.length=0
    goodReviews.length=0
    badReviews.length=0
    document.querySelector('#table-container').replaceChildren()
    document.querySelector('#pagination-container').style.display="none"
    document.querySelector('.search-container').style.display="none"
    currentPage = 1;
    currentStart = 1;
}
let sortDirection = {}; // объект для хранения направления сортировки для каждой колонки

function makeSortable() {
    const headers = document.querySelectorAll('th');
    headers.forEach((th, index) => {
        // только для нужных колонок: ID, Name, Email
        if (index <= 2) {
            th.style.cursor = 'pointer';
            th.addEventListener('click', () => {
                const keyMap = ['idDTO', 'usernameDTO', 'emailDTO']; // ключи для сортировки
                const key = keyMap[index];
                const tbody = document.querySelector('tbody');
                const rows = Array.from(tbody.querySelectorAll('tr'));

                // определяем направление сортировки
                sortDirection[key] = !sortDirection[key];

                rows.sort((a, b) => {
                    let aValue = a.dataset[key] || a.children[index].innerText;
                    let bValue = b.dataset[key] || b.children[index].innerText;

                    // если это число, преобразуем
                    if (!isNaN(aValue) && !isNaN(bValue)) {
                        aValue = Number(aValue);
                        bValue = Number(bValue);
                    }

                    if (aValue < bValue) return sortDirection[key] ? -1 : 1;
                    if (aValue > bValue) return sortDirection[key] ? 1 : -1;
                    return 0;
                });

                // вставляем отсортированные строки обратно
                rows.forEach(row => tbody.appendChild(row));
            });
        }
    });
}
async function save(typeTable){
    let url=''
    let tmpArray=[]
    if(typeTable=='blocked'){
        url='/api/admin/unBlockUser'
        tmpArray=userForBlock
        await saveRequest(url,tmpArray)
    }
    if(typeTable=='active'){
        url='/api/admin/blockUser'
        tmpArray=userForUnBlock
        await saveRequest(url,tmpArray)
    }
    if(typeTable=='temp'){
        url='/api/admin/unBlockLockedUser'
        tmpArray=userForBlock
        await saveRequest(url,tmpArray)
    }
    if(typeTable=='find'){
        if(userForBlock.length!=0){
            url='/api/admin/unBlockUser'
            tmpArray=userForBlock
            await saveRequest(url,tmpArray)
        }
        if(userForUnBlock.length!=0){
            url='/api/admin/blockUser'
            tmpArray=userForUnBlock
            await saveRequest(url,tmpArray)
        }
    }
    reverse()

}

async function saveRequest(url,array){
    if(array.length==0){
        confirm('Не выбрано значений!')
        return
    }
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('tokenAdmin'),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(array)
        });

        if (!response.ok) {
            throw new Error(`Ошибка запроса: ${response.status}`);
        }

        if (response.ok) {
            alert('Успешно!');
        }


    } catch (error) {
        console.error('Ошибка при блокировке пользователей:', error);
        alert('Не удалось выполнить действие!');
    }

}