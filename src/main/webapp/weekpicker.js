/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 * - Oh rly?
 */
$(document).ready(function() {
    var startDate;
    var endDate;
    var numberOfDays = 6;
    var date;
    var dateFormat;
    var settings;
    var dayOfTheWeek;
    var DATE_FORMAT_FOR_DISPLAY = 'dd MM yy';

    var selectCurrentWeek = function() {
        window.setTimeout(function () {
            $('.week-picker').find('.ui-datepicker-current-day a').addClass('ui-state-active')
        }, 1);
    }

    $('#form\\:weekSpinner_input').change(function() {  
        numberOfDays = 6 + ((this.value - 1) * 7)
        
        endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - dayOfTheWeek + numberOfDays);
        selectCurrentWeek();
        
        $('.endDate').val($.datepicker.formatDate(dateFormat, endDate, settings));
        $('#form\\:resTo').text($.datepicker.formatDate(DATE_FORMAT_FOR_DISPLAY, endDate, settings ));
        $('.week-picker').datepicker("refresh");
    });

    $('.week-picker').datepicker( {
        showOtherMonths: true,
        selectOtherMonths: true,
        minDate: 0,
        firstDay: 1,
        onSelect: function(dateText, inst) { 
            $('#step-two').css({
                'display': 'block'
            });
            
            settings = inst.settings;

            date = $(this).datepicker('getDate');
            
            dayOfTheWeek = date.getDay();
            if (dayOfTheWeek == 0) {
                dayOfTheWeek = 6;
            }
            else {
                dayOfTheWeek -= 1;
            }
            
            startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - dayOfTheWeek);
            endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - dayOfTheWeek + numberOfDays);
            dateFormat = inst.settings.dateFormat || $.datepicker._defaults.dateFormat;
            
            $('.startDate').val($.datepicker.formatDate(dateFormat, startDate, settings ));    
            $('.endDate').val($.datepicker.formatDate(dateFormat, endDate, settings ));   
            
            $('#form\\:resFrom').text($.datepicker.formatDate(DATE_FORMAT_FOR_DISPLAY, startDate, settings ));
            $('#form\\:resTo').text($.datepicker.formatDate(DATE_FORMAT_FOR_DISPLAY, endDate, settings ));
            
            selectCurrentWeek();
            
            $('.butt').attr("disabled", false);
        },
        beforeShowDay: function(date) {
            var cssClass = '';
            
            if(date >= startDate && date <= endDate)
                cssClass = 'ui-datepicker-current-day';
            return [true, cssClass];
        },
        onChangeMonthYear: function(year, month, inst) {
            selectCurrentWeek();
        }
    });

    $('.week-picker .ui-datepicker-calendar tr').live('mousemove', function() { $(this).find('td a').addClass('ui-state-hover'); });
    $('.week-picker .ui-datepicker-calendar tr').live('mouseleave', function() { $(this).find('td a').removeClass('ui-state-hover'); });
    
    
});

function continueClicked() {
    $("#review-form").css({
        'display': 'block'
    });
    
    $("#form").css({
        'display': 'none'
    });
}

function editClicked() {
     $("#form").css({
        'display': 'block'
    });
    
    $("#review-form").css({
        'display': 'none'
    });
}


