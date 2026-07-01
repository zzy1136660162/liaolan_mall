const fs = require('fs');
const content = fs.readFileSync('e:\\liaolan\\four-level-category\\pages\\four-level-category.html', 'utf8');

function getAttr(tag, name) {
  const match = tag.match(new RegExp('\\b' + name + '=["\']([^"\']*)["\']', 'i'));
  return match ? match[1] : '';
}

// Use the SAME regex as the validation script
const divRegex = /(<div\b[^>]*>)([\s\S]*?)<\/div>/gi;
let match;
let found = 0;

while ((match = divRegex.exec(content)) !== null) {
  const tag = match[1];
  const inner = match[2].trim();
  const cls = getAttr(tag, 'class');
  const hasCapsuleSize = /\bw-\[(?:87|88)px\]/.test(cls) && /\bh-8\b/.test(cls);
  if (hasCapsuleSize) {
    found++;
    console.log('FOUND CAPSULE #' + found);
    console.log('tag: ' + tag.trim());
    console.log('inner length: ' + inner.length);
    console.log('inner: ' + inner.substring(0, 300));
  }
}
if (found === 0) console.log('NO CAPSULE FOUND');

// Also list all divs that contain 'capsule' in class
console.log('\n--- All divs with capsule class ---');
const divRegex2 = /(<div\b[^>]*>)/gi;
while ((match = divRegex2.exec(content)) !== null) {
  if (match[1].includes('capsule')) {
    console.log(match[1].trim());
  }
}
