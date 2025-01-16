document$.subscribe(async () => {
    const url = 'https://api.allorigins.win/raw?url='
    const apiUrl = `https://api.allorigins.win/raw?url=${encodeURIComponent('https://codeberg.org/api/v1/repos/Andre601/asl-api/releases/latest')}`

    const repo_stats = document.querySelector('[data-md-component="source"] .md-source__repository');
    
    async function loadCodebergInfo(data) {
        const facts = document.createElement("ul");
        facts.className = "md-source__facts";
        
        const version = document.createElement("li");
        version.className = "md-source__fact md-source__fact--version";
        
        const stars = document.createElement("li");
        stars.className = "md-source__fact md-source__fact--stars";
        
        const forks = document.createElement("li");
        forks.className = "md-source__fact md-source__fact--forks";
        
        version.innerText = data["version"];
        stars.innerText = data["stars"];
        forks.innerText = data["forks"];
        
        facts.appendChild(version);
        facts.appendChild(stars);
        facts.appendChild(forks);
        
        repo_stats.appendChild(facts);
    }
    
    async function loadApiInfo(data) {
        const codeBlocks = document.querySelectorAll('[data-md-component="api-version"] pre code')
        if (codeBlocks === null)
            return;
        
        const version = data["version"];
        const regex = new RegExp('{apiVersion}', 'g');
        codeBlocks.forEach((codeBlock) => {
            var walker = document.createTreeWalker(codeBlock, NodeFilter.SHOW_TEXT);
            var node;
            while (node = walker.nextNode()) {
                node.nodeValue = node.nodeValue.replace(regex, version.substring(1));
            }
        });
    }
    
    async function loadPluginVersion(data) {
        const entries = document.querySelectorAll('code[data-md-component="plugin-version"]');
        if(entries === null)
            return;
        
        const version = data["version"];
        const regex = new RegExp('{version}', 'g');
        entries.forEach((entry) => {
            var walker = document.createTreeWalker(entry, NodeFilter.SHOW_TEXT);
            var node;
            while (node = walker.nextNode()) {
                node.nodeValue = node.nodeValue.replace(regex, version.substring(1));
            }
        })
    }
    
    async function fetchApiInfo() {
        const tag = await fetch(`${apiUrl}`)
            .then(_ => _.json());
        
        const data = {
            "version": tag.tag_name
        };
        
        __md_set("__api_tag", data, sessionStorage);
        loadApiInfo(data);
    }
    
    async function fetchInfo() {
        const [release, repo] = await Promise.all([
            fetch(`${url}${encodeURIComponent('https://codeberg.org/api/v1/repos/Andre601/AdvancedServerList/releases/latest')}`).then(_ => _.json()),
            fetch(`${url}${encodeURIComponent('https://codeberg.org/api/v1/repos/Andre601/AdvancedServerList')}`).then(_ => _.json()),
        ]);
        
        const data = {
            "version": release.tag_name,
            "stars": repo.stars_count,
            "forks": repo.forks_count
        };
        
        __md_set("__git_repo", data, sessionStorage);
        loadCodebergInfo(data);
        loadPluginVersion(data);
    }
    
    if(!document.querySelector('[data-md-component="source"] .md-source__facts')) {
        const cached = __md_get("__git_repo", sessionStorage);
        if((cached != null) && (cached["version"])) {
            loadCodebergInfo(cached);
            loadPluginVersion(cached);
        } else {
            fetchInfo();
        }
    }
    
    if(location.href.includes('/api/')) {
        const cachedApi = __md_get("__api_tag", sessionStorage);
        if((cachedApi != null) && (cachedApi["version"])) {
            loadApiInfo(cachedApi);
        } else {
            fetchApiInfo();
        }
    }
})