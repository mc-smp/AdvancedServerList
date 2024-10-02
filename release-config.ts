export default {
    commentOnReleasedPullRequests: false,
    changeTypes: [
        {
            title: "Breaking Changes",
            labels: ["PR Type/Breaking Change"],
            bump: "major"
        },
        {
            title: "Enhancements",
            labels: ["PR Type/Enhancement"],
            bump: "minor"
        },
        {
            title: "Bugfixes",
            labels: ["PR Type/Bug fix"],
            bump: "patch"
        },
        {
            title: "Misc",
            labels: ["PR Type/Dependency Update"],
            bump: "patch",
            default: true
        },
    ],
    skipLabels: ["PR Target/MkDocs", "PR Target/Woodpecker-CI"]
};