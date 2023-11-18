<!doctype html>
<html lang="en" data-bs-theme="auto">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>StarkExport</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@docsearch/css@3">
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">

    <script src="resources/js/color-modes.js"></script>

    <script>
        function getTransaction(from, length){
            var addr = document.form.address.value;
            var btn = document.getElementById('btnSubmit');

            var start = addr.substring(0, 2);

            if (addr === '' || start !== '0x'){
                btn.disabled = false;
                btn.childNodes[0].nodeValue= "Download";
                alert('Please enter Starknet Address');
                return;
            }

            var output = document.form.output.value.toLowerCase();

            window.location = '/account/transactions?address=' + addr + '&output=' + output;
            btn.disabled = false;
            btn.childNodes[0].nodeValue= "Download";
        }

    </script>


    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }

        .b-example-divider {
            width: 100%;
            height: 3rem;
            background-color: rgba(0, 0, 0, .1);
            border: solid rgba(0, 0, 0, .15);
            border-width: 1px 0;
            box-shadow: inset 0 .5em 1.5em rgba(0, 0, 0, .1), inset 0 .125em .5em rgba(0, 0, 0, .15);
        }

        .b-example-vr {
            flex-shrink: 0;
            width: 1.5rem;
            height: 100vh;
        }

        .bi {
            vertical-align: -.125em;
            fill: currentColor;
        }

        .nav-scroller {
            position: relative;
            z-index: 2;
            height: 2.75rem;
            overflow-y: hidden;
        }

        .nav-scroller .nav {
            display: flex;
            flex-wrap: nowrap;
            padding-bottom: 1rem;
            margin-top: -1px;
            overflow-x: auto;
            text-align: center;
            white-space: nowrap;
            -webkit-overflow-scrolling: touch;
        }

        .btn-bd-primary {
            --bd-violet-bg: #712cf9;
            --bd-violet-rgb: 112.520718, 44.062154, 249.437846;

            --bs-btn-font-weight: 600;
            --bs-btn-color: var(--bs-white);
            --bs-btn-bg: var(--bd-violet-bg);
            --bs-btn-border-color: var(--bd-violet-bg);
            --bs-btn-hover-color: var(--bs-white);
            --bs-btn-hover-bg: #6528e0;
            --bs-btn-hover-border-color: #6528e0;
            --bs-btn-focus-shadow-rgb: var(--bd-violet-rgb);
            --bs-btn-active-color: var(--bs-btn-hover-color);
            --bs-btn-active-bg: #5a23c8;
            --bs-btn-active-border-color: #5a23c8;
        }

        .bd-mode-toggle {
            z-index: 1500;
        }

        .bd-mode-toggle .dropdown-menu .active .bi {
            display: block !important;
        }

    </style>


    <!-- Custom styles for this template -->
    <link href="resources/css/checkout.css" rel="stylesheet">
</head>
<body class="bg-body-tertiary">

<div class="dropdown position-fixed bottom-0 end-0 mb-3 me-3 bd-mode-toggle">
    <button class="btn btn-bd-primary py-2 dropdown-toggle d-flex align-items-center"
            id="bd-theme"
            type="button"
            aria-expanded="false"
            data-bs-toggle="dropdown"
            aria-label="Toggle theme (auto)">
        <svg class="bi my-1 theme-icon-active" width="1em" height="1em"><use href="#circle-half"></use></svg>
        <span class="visually-hidden" id="bd-theme-text">Toggle theme</span>
    </button>
    <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="bd-theme-text">
        <li>
            <button type="button" class="dropdown-item d-flex align-items-center" data-bs-theme-value="light" aria-pressed="false">
                <svg class="bi me-2 opacity-50 theme-icon" width="1em" height="1em"><use href="#sun-fill"></use></svg>
                Light
                <svg class="bi ms-auto d-none" width="1em" height="1em"><use href="#check2"></use></svg>
            </button>
        </li>
        <li>
            <button type="button" class="dropdown-item d-flex align-items-center" data-bs-theme-value="dark" aria-pressed="false">
                <svg class="bi me-2 opacity-50 theme-icon" width="1em" height="1em"><use href="#moon-stars-fill"></use></svg>
                Dark
                <svg class="bi ms-auto d-none" width="1em" height="1em"><use href="#check2"></use></svg>
            </button>
        </li>
        <li>
            <button type="button" class="dropdown-item d-flex align-items-center active" data-bs-theme-value="auto" aria-pressed="true">
                <svg class="bi me-2 opacity-50 theme-icon" width="1em" height="1em"><use href="#circle-half"></use></svg>
                Auto
                <svg class="bi ms-auto d-none" width="1em" height="1em"><use href="#check2"></use></svg>
            </button>
        </li>
    </ul>
</div>


<div class="container">
    <main>
        <div class="py-5 text-left">
            <h2>StarkExport</h2>
            <p class="lead">Effortlessly effort Starknet transactions to CSV, XLSX, JSON and more</p>
        </div>

        <div class="row g-5">
            <div class="col-md-7 col-lg-8">
                <form id="form" name="form">
                    <div class="row g-3">

                        <div class="col-12">
                            <label for="address" class="form-label">Contract Address</label>
                            <input type="text" class="form-control" id="address" placeholder="0x..">
                        </div>

                        <div class="col-12">
                            <label for="output" class="form-label">Format</label>
                            <select class="form-select" id="output" required>
                                <option>XLSX</option>
                                <option>CSV</option>
                                <option>TXT</option>
                                <option>ODS</option>
                            </select>
                        </div>
                        <div class="col-3">
                            <button class="btn btn-primary" type="button" id="btnSubmit" onclick="this.disabled=true;this.childNodes[0].nodeValue = 'Wait...';getTransaction();">Download</button>
                        </div>

                    </div>
                </form>
            </div>
        </div>
    </main>
    <footer class="text-end mt-lg-5">
        <!-- Copyright -->
        <div class="text-end">
            <a href="https://github.com/mylastwanto/StarkExport">Github</a>
        </div>
        <!-- Copyright -->
    </footer>
    <!-- Footer -->

</div>
<script src="resources/js/bootstrap.bundle.min.js"></script>

</body>
</html>
