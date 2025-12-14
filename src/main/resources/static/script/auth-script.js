// Получаем кнопки и контейнеры
const showRegisterBtn = document.getElementById('showRegister');
const showVhodBtn = document.getElementById('showVhod');
const cancelRegisterBtn = document.getElementById('cancelRegister');
const cancelVhodBtn = document.getElementById('cancelVhod');
const mainContainer = document.getElementById('mainContainer');
const registerForm = document.getElementById('registerForm');
const vhodForm = document.getElementById('vhodForm');
let countBad=0
document.addEventListener('load',()=>
sessionStorage.clear(),
    countBad=0
)
// Показать форму регистрации
showRegisterBtn.addEventListener('click', () => {
    mainContainer.style.display = 'none';
    const form = document.querySelector('.reg');
   form.querySelectorAll('input').forEach(input=>input.value='')
    registerForm.style.display = 'block';
});

showVhodBtn.addEventListener('click', () => {
    mainContainer.style.display = 'none';
    const form = document.querySelector('.vhod');
    form.querySelectorAll('input').forEach(input=>input.value='')
    vhodForm.style.display = 'block';
});

// Скрыть форму регистрации и вернуть основной контейнер
cancelRegisterBtn.addEventListener('click', () => {

    registerForm.style.display = 'none';
    mainContainer.style.display = 'block';
});

cancelVhodBtn.addEventListener('click', () => {

    vhodForm.style.display = 'none';
    mainContainer.style.display = 'block';
});
// Получаем форму и поля пароля
const form = registerForm.querySelector('form');
const passwordInput = form.querySelectorAll('input[type="password"]');

// Проверка совпадения паролей при отправке формы
form.addEventListener('submit', (e) => {
    if (passwordInput[0].value !== passwordInput[1].value) {
        e.preventDefault(); // отменяем отправку формы
        alert("Пароли не совпадают!"); // выводим сообщение
    }
    // Если пароли совпадают, форма отправится
});


// Получаем форму входа
const vhodFormElement = document.querySelector('.vhod');

vhodFormElement.addEventListener('submit', async (e) => {
    e.preventDefault(); // остановка стандартного submit

    sessionStorage.clear(); // очищаем старые данные

    const data = Object.fromEntries(new FormData(vhodFormElement));

    // Можно добавить базовую валидацию (например, email и пароль не пустые)
    if (!data.email || !data.password) {
        alert('Заполните все поля!');
        return;
    }

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.status === 403) {
            const errorMessage = await response.json();
            const modal = document.getElementById("errorModal");
            const modalMessage = document.getElementById("modalMessage");
            const modalOkBtn = document.getElementById("modalOkBtn");

            let text = '';
            if (errorMessage.message.includes("blocked")) {
                text = "Аккаунт заблокирован! <br> Обратитесь в тех. поддержку!";
            } else if (errorMessage.message.includes("locked")) {
                text = "Аккаунт разблокируется в течение 15 минут!";
            }

            modalMessage.innerHTML = text;
            modal.style.display = "flex";

            modalOkBtn.onclick = () => {
                modal.style.display = "none";
                mainContainer.style.display = 'block';
                registerForm.style.display = 'none';
                vhodForm.style.display = 'none';
            };

            return;
        }
if(response.status==401 ){
    showModal('Данные не верны!')
    return
}
        const result = await response.json();



        // Определяем URL и ключ токена
        let url = '';
        let tokenKey = '';
        if (result.role === 'ROLE_ADMIN') {
            url = '/api/admin/panel';
            tokenKey = 'tokenAdmin';
        } else if (result.role === 'ROLE_USER') {
            url = '/api/user/panel';
            tokenKey = 'tokenUser' ;
        }

        // Загружаем панель
        const res = await fetch(url, {
            headers: { 'Authorization': 'Bearer ' + result.token }
        });

        if (!res.ok) throw new Error('Не удалось загрузить панель.');

        const html = await res.text();
        sessionStorage.setItem(tokenKey, result.token);
        sessionStorage.setItem('email', result.email);

        document.open();
        document.write(html);
        document.close();

    } catch (error) {
        console.log(error)
    }
});


const regForm = document.querySelector('.reg');

regForm.addEventListener('submit', async (e) => {


    if (!regForm.checkValidity()) {
        return; // браузер сам покажет ошибки
    }

    e.preventDefault();


    const [pass1, pass2] = regForm.querySelectorAll('input[type="password"]');
    if (pass1.value !== pass2.value) {
        pass2.setCustomValidity('Пароли не совпадают');
        pass2.reportValidity();
        pass2.setCustomValidity('');
        return;
    }

    // 3️⃣ Отправка
    await register();
});

async function register() {
    sessionStorage.clear();

    const form = document.querySelector('.reg');
    const data = Object.fromEntries(new FormData(form));

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.status === 409) {
            showModal('Данный email уже используется!');
            return;
        }

        const result = await response.json();
        await loadUserPanel(result);

    } catch (err) {
        console.error(err);
        alert('Ошибка регистрации');
    }
}


async function loadUserPanel(result) {
    try {
        // Определяем URL и токен для сессии
        let url = '/api/user/panel';
        let tokenKey = 'tokenUser';

        // Запрос панели пользователя с токеном
        const res = await fetch(url, {
            headers: { 'Authorization': 'Bearer ' + result.token }
        });

        if (!res.ok) throw new Error('Не удалось загрузить панель пользователя.');

        const html = await res.text();

        // Сохраняем токен и email в sessionStorage
        sessionStorage.setItem(tokenKey, result.token);
        sessionStorage.setItem('email', result.email);

        // Заменяем страницу на панель
        document.open();
        document.write(html);
        document.close();

    } catch (error) {
        // Если что-то пошло не так — удаляем данные и выводим ошибку
        sessionStorage.removeItem('tokenUser');
        sessionStorage.removeItem('email');
        alert(error);
    }
}

function showModal(message) {
    const modal = document.getElementById("errorModal");
    const modalMessage = document.getElementById("modalMessage");
    const modalOkBtn = document.getElementById("modalOkBtn");

    modalMessage.innerHTML = message;
    modal.style.display = "flex"; // показываем модалку

    modalOkBtn.onclick = () => {
        modal.style.display = "none"; // скрываем модалку
        // можно вернуть основной контейнер и скрыть формы
        const mainContainer = document.getElementById('mainContainer');
        const registerForm = document.getElementById('registerForm');
        const vhodForm = document.getElementById('vhodForm');

        mainContainer.style.display = 'block';
        registerForm.style.display = 'none';
        vhodForm.style.display = 'none';
    }
}






