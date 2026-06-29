$eventId = "5b6bc185-eea9-4072-ba71-67b5fc453af3"
$url = "http://localhost:8080/api/v1/orders"
$concurrentRequests = 50

$jobs = @()
$stopwatch = [System.Diagnostics.Stopwatch]::StartNew()

for ($i = 0; $i -lt $concurrentRequests; $i++) {
    $body = @{
        eventId = $eventId
        seatCount = 1
        email = "user$i@example.com"
    } | ConvertTo-Json

    $jobs += Start-Job -ScriptBlock {
        param($url, $body)
        try {
            $response = Invoke-RestMethod -Uri $url -Method Post -Body $body -ContentType "application/json"
            return @{ Success = $true; Response = $response }
        } catch {
            return @{ Success = $false; Error = $_.Exception.Message }
        }
    } -ArgumentList $url, $body
}

$results = $jobs | Wait-Job | Receive-Job
$stopwatch.Stop()

$succeeded = ($results | Where-Object { $_.Success }).Count
$failed = ($results | Where-Object { -not $_.Success }).Count

Write-Host "Total requests: $concurrentRequests"
Write-Host "Accepted (202): $succeeded"
Write-Host "Errored: $failed"
Write-Host "Total time: $($stopwatch.ElapsedMilliseconds) ms"
Write-Host "Throughput: $([math]::Round($concurrentRequests / ($stopwatch.ElapsedMilliseconds / 1000), 2)) requests/sec"

$jobs | Remove-Job