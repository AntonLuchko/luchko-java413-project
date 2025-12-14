async function ticket(){

        try {
            const token = sessionStorage.getItem('tokenUser'); // получаем токен из sessionStorage

            const response = await fetch('/api/user/ticketPanel', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error(`Ошибка при переходе на страницу покупки билетов: ${response.status} ${response.statusText}`);
            }

            const html = await response.text();
            document.querySelector('main').innerHTML = html;

        } catch (error) {
            console.error('Ошибка в ticket():', error);
            alert(error.message);
        }
}

function acrivBtn(){
    document.getElementById('tb-ticketForm').addEventListener('submit', async function () {
        event.preventDefault(); // предотвращаем стандартную отправку формы

        const confirmation = document.getElementById('tb-confirmation');

        // Получаем данные формы
        const ticketData = {
            from: document.getElementById('tb-from').value,
            to: document.getElementById('tb-to').value,
            date: document.getElementById('tb-date').value,
            type: document.getElementById('tb-type').value
        };
        console.log(JSON.stringify(ticketData))
        try {
            const response = await fetch('/api/user/saveTicket', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenUser')
                },
                body: JSON.stringify(ticketData)
            });

            if (!response.ok) {
                throw new Error(`Ошибка сервера: ${response.status} ${response.statusText}`);
            }

            const result = await response.json();
            if(result==1){
                confirmation.style.display = 'block';
                confirmation.textContent = `Билет успешно забронирован!`;
                this.reset();
            } else{
                confirmation.style.display = 'block';
                confirmation.textContent = `Билет не забронирован! Повторите попытку.`;
                this.reset(); // сброс формы
            }

        } catch (error) {
            console.error('Ошибка при отправке билета:', error);
            confirmation.style.display = 'block';
            confirmation.style.backgroundColor = '#f8d7da';
            confirmation.style.color = '#721c24';
            confirmation.textContent = 'Не удалось забронировать билет. Попробуйте позже.';
        }
    });

}
