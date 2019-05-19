var logTypeSelect = document.querySelector('.log-type-selection');
var logTableBody = document.querySelector('.log-table-body');
logTypeSelect.disabled = true;

//<editor-fold defaultstate="collapsed" desc="code for select box">

var xmlhttp = new XMLHttpRequest();
var url = '/InventorySystem/SystemController';
var param = '?command=LOG_TYPE';
xmlhttp.open("GET", url + param, true);

xmlhttp.onreadystatechange = function() {
    if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
        var result = JSON.parse(this.responseText);
        console.log('result', result);

        var optionPlaceHolder = document.createElement('option');
        optionPlaceHolder.innerHTML = 'Select a log type';
        optionPlaceHolder.disabled = true;
        optionPlaceHolder.style.display = 'none';
        optionPlaceHolder.selected = true;
        logTypeSelect.appendChild(optionPlaceHolder);

        result.forEach(function(log) {
           var option = document.createElement('option');

           option.innerHTML = capitalize(log.type).replace(/-/g, ' ');
           option.value = log.id;

           logTypeSelect.appendChild(option);

        });

    }
}

xmlhttp.send();

logTypeSelect.onchange = function() {
    console.log('selectbox on change');
    var filter, table, tr, td, textValue;
    
    filter = this.options[this.selectedIndex].text.toLowerCase();
    table = document.getElementById('log-table');
    tr = table.getElementsByTagName('tr');
    
    for(var i = 0; i < tr.length; i++) {
        
        td = tr[i].getElementsByTagName('td')[1];
        
        if(td) {
            textValue = td.textContent || td.innerText;
            console.log('textValue', textValue);
            
            if (textValue.toLowerCase().indexOf(filter) > -1) {
                tr[i].style.display = '';
            }else {
                tr[i].style.display = 'none';
            }
        }
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="code for loading all logs in first load">
var xmlhttp = new XMLHttpRequest();
var url = '/InventorySystem/SystemController';
var param = '?command=LOGS';
xmlhttp.open('GET', url + param, true)
xmlhttp.onreadystatechange = function() {
    if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
        logTypeSelect.disabled = false;
        console.log('result', JSON.parse(this.responseText));
        var result = JSON.parse(this.responseText);
        
        result.forEach(function(log) {
            var row = document.createElement('tr');
            
            var userTd = document.createElement('td');
            userTd.innerHTML = log.username;
            row.appendChild(userTd);
            
            var logTd = document.createElement('td');
            logTd.innerHTML = capitalize(log.type).replace(/-/g, ' ');
            row.appendChild(logTd);
            
            var timestampTd = document.createElement('td');
            timestampTd.innerHTML = log.timestamp;
            row.appendChild(timestampTd);
            
            logTableBody.appendChild(row);
            
        });
        
    }
}

xmlhttp.send();
//</editor-fold>
