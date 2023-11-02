import tempfile

import win32api
import win32print


win32api.ShellExecute(
    0,
    "print",
    'C:/Users/Sysadm/PycharmProjects/pythonProject3/result.pdf',
    #
    # If this is None, the default printer will
    # be used anyway.
    #
    '/d:"%s"' % win32print.GetDefaultPrinter(),
    ".",
    0
)
