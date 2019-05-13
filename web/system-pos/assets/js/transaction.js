var transactionBttn = document.querySelector('.pos-transaction-bttn');
var transactionModal = document.getElementById('pos-transaction-modal');
var transactionModalClose = document.querySelector('.transaction-modal-close');
var transactionModalTbody = document.querySelector('#pos-transaction-modal tbody');
var transactionDetailModal = document.getElementById('pos-transaction-detail-modal');
var transactionDetailModalClose = document.querySelector('.transaction-detail-modal-close');
var transactionDetailModalTbody = document.querySelector('#pos-transaction-detail-modal tbody');


transactionBttn.onclick = function() {
    transactionModal.style.display = 'block';
    removeElemChild(transactionModalTbody);
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_TRANSACTIONS';
    
    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function() {
        
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
            
            result.forEach(function(item) {
                var row = document.createElement('tr');
                row.dataset['transactionId'] = item.id;
                
                var fields = [item.type.name, item.totalAmount, item.totalQuantity, item.timestamp];
                
                fields.forEach(function(item) {
                    var cell = document.createElement('td');
                    cell.innerHTML = item;
                    row.appendChild(cell);
                });
                
                transactionModalTbody.appendChild(row);
            });
        }
    }
    
    xmlhttp.send();
}

transactionModalClose.onclick = function() {
    transactionModal.style.display = 'none';
}

transactionModalTbody.onclick = function(e) {
    transactionDetailModal.style.display = 'block';
    console.log('target', e.target.parentNode.dataset['transactionId']);
    var targetId = e.target.parentNode.dataset['transactionId'];
    var param = '?command=TRANSACTION_DETAILS_BY_ID&id=' + targetId;
    

    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=TRANSACTION_DETAILS_BY_ID&id=' + targetId;

    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function () {

        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            removeElemChild(transactionDetailModalTbody);
            console.log('result new', JSON.parse(this.responseText));
            var result = JSON.parse(this.responseText);
            
            result.forEach(function(detail) {
//                console.log('detail', detail.item.name);
                var row = document.createElement('tr');
                var cells = [detail.item.name, 
                    detail.item.brand.name, 
                    detail.item.category.name, 
                    detail.item.price, 
                    detail.item.resellerPrice, 
                    detail.quantity,
                    detail.totalAmount
                ];
                
                cells.forEach(function(item) {
                    console.log(cell);
                    var cell = document.createElement('td');
                    cell.innerHTML = item;
                    row.appendChild(cell);
                });
//                
                transactionDetailModalTbody.appendChild(row);
            });
        }
    }

    xmlhttp.send();
}

transactionDetailModalClose.onclick = function() {
    transactionDetailModal.style.display = 'none';
}