async function myTicketPanel() {
    try {

        const token = sessionStorage.getItem('tokenUser');

        const response = await fetch('/api/user/myTicketPanel', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка при загрузке панели билетов: ${response.status} ${response.statusText}`);
        }


        const html = await response.text();
        document.querySelector('main').innerHTML = html;
        loadMyTickets();

    } catch (error) {
        console.error('Ошибка в myTicketPanel():', error);
        alert(error.message);
    }
}

async function loadMyTickets() {
    try {
        const token = sessionStorage.getItem('tokenUser');

        const response = await fetch('/api/user/myTicket', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status === 204) {
            return;
        }

        if (!response.ok) {
            throw new Error(`Ошибка при получении билетов: ${response.status} ${response.statusText}`);
        }


        const tickets = await response.json();

        // Находим tbody таблицы
        const tbody = document.querySelector('.mytb-table tbody');
        tbody.innerHTML = ''; // очищаем старые строки

        // Заполняем таблицу данными
        tickets.forEach(ticket => {
            const tr = document.createElement('tr');
            tr.classList.add('mytb-tr');

            tr.innerHTML = `
                <td class="mytb-td">${ticket.from}</td>
                <td class="mytb-td">${ticket.to}</td>
                <td class="mytb-td">${ticket.type}</td>
                <td class="mytb-td">${ticket.date}</td>
            `;

            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error('Ошибка при загрузке билетов:', error);
        alert('Не удалось загрузить билеты. Попробуйте позже.');
    }
}



