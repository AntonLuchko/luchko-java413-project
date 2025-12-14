async function oznakom(id) {
    try {
        const response = await fetch(`/api/user/notification/read?id=${id}`, {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('tokenUser')
            }
        });

        if (!response.ok) {
            throw new Error('Не удалось отметить уведомление');
        }

        // 🔎 находим элемент по data-id
        const el = document.querySelector(`.notification-text[data-id="${id}"]`);
        if (el) {
            el.style.fontWeight = 'normal'
        }

    } catch (error) {
        console.error('Ошибка ознакомления:', error);
        alert('Ошибка при обработке уведомления');
    }
}


async function delNotif(id) {
    try {
        const response = await fetch(`/api/user/notification/delete?id=${id}`, {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('tokenUser')
            }
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить уведомление');
        }

        // 🔎 находим элемент по data-id и удаляем из DOM
        const el = document.querySelector(`.notification-text[data-id="${id}"]`);if (el) {
            const actions = el.nextElementSibling; // div с кнопками
            if (actions && actions.classList.contains('notification-actions')) {
                actions.remove();
            }
            el.remove();
        }


    } catch (error) {
        console.error('Ошибка удаления уведомления:', error);
        alert('Ошибка при удалении уведомления');
    }
}

async function emtyActive() {
    try {
        const token = sessionStorage.getItem('tokenUser');


        const response = await fetch('/api/user/activeNotificationsCount', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка сервера: ${response.status} ${response.statusText}`);
        }


        const data = await response.json();

        return data
    } catch (error) {
        console.error('Ошибка при получении активных уведомлений:', error);
        return 0;
    }
}


async function notif(){
    clearInterval(signal)
    const block = document.querySelector('.notification-wrapper');
    if (block) {
        block.style.backgroundColor = 'transparent';
    }
    try {
        const response = await fetch('/api/user/notificationPanel', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ` +sessionStorage.getItem('tokenUser')// добавляем токен
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка: ${response.status} ${response.statusText}`);
        }
        const data = await response.text(); // если возвращается HTML

        document.querySelector('main').innerHTML=data
        listNotific()
    } catch (error) {
        console.error('Ошибка при получении уведомлений!', error);
    }
}

async function listNotific(){
    const token = sessionStorage.getItem('tokenUser');

    try {
        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        };

        // 🔹 параллельные запросы
        const [listRes, countRes] = await Promise.all([
            fetch(`/api/user/myNotific`, { headers }),
            fetch(`/api/user/countMyNotific`, { headers })
        ]);

        // если уведомлений нет
        if (listRes.status === 204) {
            return {
                list: [],
                totalCount: 0
            };
        }

        if (!listRes.ok || !countRes.ok) {
            throw new Error('Ошибка при получении уведомлений');
        }

        const list = await listRes.json();      // список уведомлений
        const totalCount = await countRes.json(); // общее количество

        setNotific(list)

    } catch (error) {
        console.error('Ошибка listNotific:', error);

    }
}
function setNotific(list){
    let  tableNotific=document.querySelector('.notification-item')
    for (let i = 0; i < list.length; i++) {
        let active=''
        let str=''
        if(list[i].active==true) {
            str=` <b class="notification-text" data-id="${list[i].id}">${list[i].message}</b>`
        } else str=`<p class="notification-text">${list[i].message}</p>`
        tableNotific.insertAdjacentHTML("beforeend",`${str}
    <div class="notification-actions">
        <button class="btn-read" onclick="oznakom(${list[i].id})">Ознакомился</button>
        <button class="btn-delete" onclick="delNotif(${list[i].id})">Удалить</button>
    </div>`)
    }
}