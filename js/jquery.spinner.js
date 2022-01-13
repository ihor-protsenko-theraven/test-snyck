(function ($) {

  var methods = {
    init: function (options) {
      var settings = $.extend({}, $.fn.spinner.defaults, options || {});

      return this.each(function () {
        var $this, data;

        $this = $(this);
        data  = $this.data('spinner');

        if (!data) {
          data = $.extend({}, settings, { interVal: null });
          $this.data('spinner', data).addClass('f' + data.step);
        }

        methods.run.apply($this);
      });
    },
    run: function () {
      var $this, data, timing;

      $this     = this;
      data      = this.data('spinner');
      timing    = 1000 / data.fps;

      methods.stop.apply($this);

      data.interVal = setInterval(function () {
        var prevStep;

        if (++data.step > data.steps) {
          data.step = 1;
        }
        prevStep = (data.step - 1 <= 0) ? data.steps : data.step - 1;

        $this.removeClass('f' + prevStep).addClass('f' + data.step);
      }, timing);

      return this;
    },
    stop: function () {
      var data = this.data('spinner');

      if (data.interVal) {
        clearInterval(data.interVal);
        data.interVal = null;
      }

      return this;
    },
    destroy: function () {
      methods.stop.apply(this);
      this.removeClass('f' + this.data('spinner').step);
      this.removeData('spinner');

      return this;
    }
  }

  $.fn.spinner = function (method) {

    if (methods[method]) {
      return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
    } else if(typeof method === 'object' || !method) {
      return methods.init.apply(this, arguments);
    } else {
      $.error('Method ' + method + ' does not exixt in jQuery.spinner');
    }
  }

  $.fn.spinner.defaults = {
    fps: 24,
    steps: 24,
    step: 1
  }
})(jQuery);
