


let tmpInnerOffice=document.querySelector('main').innerHTML
let signal




document.addEventListener('DOMContentLoaded', async () => {
    start();

    // Получаем количество активных уведомлений
    const count = await emtyActive();

    if (count >= 1) {
        const block = document.querySelector('.notification-wrapper');
        if (!block) return;

        let isRed = false;
        signal = setInterval(() => {
            if (isRed) {
                block.style.backgroundColor = '';
            } else {
                block.style.backgroundColor = 'red';
            }
            isRed = !isRed;
        }, 700);
    }
});

document.querySelector('header').addEventListener('click',  () => {
    if(event.target.classList.contains('rev')){
        reviewsthis()
    } else if(event.target.classList.contains('exit')){
        exitOffice()
    }else if(event.target.classList.contains('notification-bell')){
        notif()
    } else if(event.target.classList.contains('office')){
        office()
        start()
    }
    else if(event.target.classList.contains('myTicket')){
        myTicketPanel()
    }
})
async function reviewsthis(){
    try {
        const response = await fetch('/api/user/reviewPanel', {
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
    } catch (error) {
        console.error('Ошибка при получении отзывов:', error);
    }
}







// Передаём токен через query-параметр
const socket = new SockJS('/ws-notifications?access_token=' + sessionStorage.getItem('tokenUser'));
const stompClient = Stomp.over(socket);

stompClient.connect(
    {}, // пустой объект заголовков — больше не нужен
    function(frame) {
        console.log('Connected: ' + frame);

        // подписка на уведомления пользователя по email
        stompClient.subscribe('/topic/notifications/' + sessionStorage.getItem('email'), function() {
            showNotification();
        });
    },
    function(error) {
        console.error('STOMP connection error: ', error);
    }
);

function showNotification() {
    const block = document.querySelector('.notification-wrapper');
    if (!block) return;

    let isRed = false;
    signal=setInterval(() => {
        if (isRed) {
            block.style.backgroundColor = '';
        } else {
            block.style.backgroundColor = 'red';
        }
        isRed = !isRed;
    }, 700);
}




async function exitOffice() {
    try {
        // Очищаем данные сессии
        sessionStorage.removeItem('tokenUser');
        sessionStorage.removeItem('email');

        // Запрашиваем страницу входа

            let response = await fetch('/api/auth/vhod')
            if (!response.ok) {
                throw new Error('Ошибка при выходе с сервера');
            }

            window.location.href = '/api/auth/vhod';

    } catch (error) {
        console.error('Ошибка при выходе из аккаунта:', error);
        alert('Не удалось выйти. Попробуйте снова.');
    }
}


function office(){
   document.querySelector('main').innerHTML=tmpInnerOffice
}









