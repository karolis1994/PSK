/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 * - Oh rly?
 */

var startDate;
var endDate;
var numberOfDays = 6;
var offset = 7;

function beforeDayRenderInline(date) {
    var dateSelected = $('.hasDatepicker').datepicker('getDate');
   
    var dayOfTheWeek = dateSelected.getDay();
    if (dayOfTheWeek == 0) {
        dayOfTheWeek = 6;
    }
    else {
        dayOfTheWeek -= 1;
    }

    startDate = new Date(dateSelected.getFullYear(), dateSelected.getMonth(), dateSelected.getDate() - dayOfTheWeek);
    endDate = new Date(dateSelected.getFullYear(), dateSelected.getMonth(), dateSelected.getDate() - dayOfTheWeek + numberOfDays);
    
    var cssClass = '';
 
    if(date >= startDate && date <= endDate) {
        if (offset <= 0 || $('.reservation-period').is(':visible')) {
            cssClass = 'ui-datepicker-current-day';
        }
        else {
            offset--;
        }        
    }
    
    return [true, cssClass];
}

function beforeDayRender(date) {

}

function spinnerChanged() {
    var spinnerValue = $('input.ui-spinner-input').val();
    numberOfDays = 6 + ((spinnerValue - 1) * 7);
}
