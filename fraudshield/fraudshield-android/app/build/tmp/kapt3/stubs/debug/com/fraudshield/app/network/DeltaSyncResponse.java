package com.fraudshield.app.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B)\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u000f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\bH\u00c6\u0003J3\u0010\u0012\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\r\u00a8\u0006\u001a"}, d2 = {"Lcom/fraudshield/app/network/DeltaSyncResponse;", "", "new_whitelist_entries", "", "Lcom/fraudshield/app/network/WhitelistEntryDto;", "new_blacklist_entries", "Lcom/fraudshield/app/network/BlacklistEntryDto;", "last_sync_timestamp", "", "(Ljava/util/List;Ljava/util/List;J)V", "getLast_sync_timestamp", "()J", "getNew_blacklist_entries", "()Ljava/util/List;", "getNew_whitelist_entries", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class DeltaSyncResponse {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.fraudshield.app.network.WhitelistEntryDto> new_whitelist_entries = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.fraudshield.app.network.BlacklistEntryDto> new_blacklist_entries = null;
    private final long last_sync_timestamp = 0L;
    
    public DeltaSyncResponse(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fraudshield.app.network.WhitelistEntryDto> new_whitelist_entries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.fraudshield.app.network.BlacklistEntryDto> new_blacklist_entries, long last_sync_timestamp) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fraudshield.app.network.WhitelistEntryDto> getNew_whitelist_entries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fraudshield.app.network.BlacklistEntryDto> getNew_blacklist_entries() {
        return null;
    }
    
    public final long getLast_sync_timestamp() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fraudshield.app.network.WhitelistEntryDto> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fraudshield.app.network.BlacklistEntryDto> component2() {
        return null;
    }
    
    public final long component3() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.fraudshield.app.network.DeltaSyncResponse copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fraudshield.app.network.WhitelistEntryDto> new_whitelist_entries, @org.jetbrains.annotations.NotNull()
    java.util.List<com.fraudshield.app.network.BlacklistEntryDto> new_blacklist_entries, long last_sync_timestamp) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}