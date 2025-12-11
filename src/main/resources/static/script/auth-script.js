// Получаем кнопки и контейнеры
const showRegisterBtn = document.getElementById('showRegister');
const showVhodBtn = document.getElementById('showVhod');
const cancelRegisterBtn = document.getElementById('cancelRegister');
const cancelVhodBtn = document.getElementById('cancelVhod');
const mainContainer = document.getElementById('mainContainer');
const registerForm = document.getElementById('registerForm');
const vhodForm = document.getElementById('vhodForm');

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


async function vhod() {
    event.preventDefault();
    let url=''
    let token=''
    let error2=''
    try {
        let form = document.querySelector('.vhod');
        const formData = new FormData(form);

        let data = {};
        for (let pair of formData.entries()) {
            data[pair[0]] = pair[1];
        }

        let jsonData = JSON.stringify(data);
        let response = await fetch("/api/auth/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if(response.status === 403){
            let errorMessage = await response.json();
            if(errorMessage.message.includes("blocked") || errorMessage.message.includes("locked")){
                const modal = document.getElementById("errorModal");
                const modalMessage = document.getElementById("modalMessage");
                const modalOkBtn = document.getElementById("modalOkBtn");

                let text = errorMessage.message.includes("blocked")
                    ? "Аккаунт заблокирован! <br> Обратитесь в тех. поддержку!"
                    : "Аккаунт разблокируется в течение 15 минут!";

                modalMessage.innerHTML = text;
                modal.style.display = "flex"; // показываем модалку

                modalOkBtn.onclick = () => {
                    modal.style.display = "none"; // скрываем модалку
                    mainContainer.style.display = 'block';
                    registerForm.style.display = 'none';
                    vhodForm.style.display = 'none';
                }
            }
            return;
        }

        // ✅ Ждём промис
        let result = await response.json();

        if (result.role === 'ROLE_ADMIN') {
            url = '/api/admin/panel'
            token = 'tokenAdmin'
            error2 = 'Не удалось загрузить панель администратора. Попробуйте снова.'
        }
            else if(result.role === 'ROLE_USER'){
            url = '/api/user/panel'
            token = 'tokenUser'+result.email
            error2 = 'Не удалось загрузить панель пользователя. Попробуйте снова.'
        }

            try {
                const res = await fetch(url, {
                    headers: { 'Authorization': 'Bearer ' + result.token }
                });
                const html = await res.text();
                sessionStorage.setItem(token, result.token);
                document.open();
                document.write(html);
                document.close();
            } catch(error) {
                if(token && token.trim() !== '') {
                    sessionStorage.removeItem(token);
                }
                alert(error);
            }


    } catch (error) {
        if(token && token.trim() !== '') {
            sessionStorage.removeItem(token);
        }
        alert(error);
    }
}

async function register() {
    event.preventDefault();

    try {
        let form = document.querySelector('.reg');
        const formData = new FormData(form);

        let data = {};
        let entries = Array.from(formData.entries());
        data = Object.fromEntries(entries.slice(0));

        let jsonData = JSON.stringify(data);
        let response = await fetch("/api/auth/register", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if (response.status === 409) { // email уже существует
            const modal = document.getElementById("errorModal");
            const modalMessage = document.getElementById("modalMessage");
            const modalOkBtn = document.getElementById("modalOkBtn");

            modalMessage.innerHTML = "Данный email уже используется!";
            modal.style.display = "flex"; // показываем модалку

            modalOkBtn.onclick = () => {
                modal.style.display = "none"; // скрываем модалку
                mainContainer.style.display = 'block';
                registerForm.style.display = 'none';
                vhodForm.style.display = 'none';
            }
            return;
        }

        // Если нужно обработать успешную регистрацию:
        let result = await response.json();
        try {
            const res = await fetch('/api/user/panel', {
                headers: { 'Authorization': 'Bearer ' + result.token }
            });
            const html = await res.text();
            sessionStorage.setItem('tokenUser'+result.email, result.token);
            document.open();
            document.write(html);
            document.close();
        } catch(error) {
            sessionStorage.removeItem('tokenUser'+result.email);
            alert(error);
        }

    } catch (error) {
        sessionStorage.removeItem('tokenUser'+result.email);
        console.error("Произошла ошибка при регистрации:", error);
        alert("Произошла ошибка при регистрации. Попробуйте снова.");
    }
}





