const express = require('express');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// Serve compiled Android APK
app.get('/app-debug.apk', (req, res) => {
  const apkPath = path.join(__dirname, 'app', 'build', 'outputs', 'apk', 'debug', 'app-debug.apk');
  const backupApkPath = path.join(__dirname, '.build-outputs', 'app-debug.apk');

  if (fs.existsSync(apkPath)) {
    res.setHeader('Content-Type', 'application/vnd.android.package-archive');
    res.setHeader('Content-Disposition', 'attachment; filename="BalajiTiffin-debug.apk"');
    res.sendFile(apkPath);
  } else if (fs.existsSync(backupApkPath)) {
    res.setHeader('Content-Type', 'application/vnd.android.package-archive');
    res.setHeader('Content-Disposition', 'attachment; filename="BalajiTiffin-debug.apk"');
    res.sendFile(backupApkPath);
  } else {
    res.status(404).send('APK build in progress or not found.');
  }
});

// Fallback to main web UI
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Balaji Tiffin Service Web & Android Server running on port ${PORT}`);
});
