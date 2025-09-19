$ErrorActionPreference = "Stop"
& .\mvnw.cmd -q -DskipTests package | Out-Null

$root = Get-Location

Start-Process -WindowStyle Minimized -WorkingDirectory "$root\services\product-service" -FilePath "$root\mvnw.cmd" -ArgumentList "-q spring-boot:run"
Start-Process -WindowStyle Minimized -WorkingDirectory "$root\services\order-service" -FilePath "$root\mvnw.cmd" -ArgumentList "-q spring-boot:run"
Start-Process -WindowStyle Minimized -WorkingDirectory "$root\gateway" -FilePath "$root\mvnw.cmd" -ArgumentList "-q spring-boot:run"
Start-Process -WindowStyle Minimized -WorkingDirectory "$root\ui" -FilePath "$root\mvnw.cmd" -ArgumentList "-q spring-boot:run"

Write-Host "UI available at http://localhost:8085"

